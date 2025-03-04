import java.util.*;

class Dominoes {
    private class Edge {
        final int node;
        final int dominoIndex;

        Edge(int node, int dominoIndex) {
            this.node = node;
            this.dominoIndex = dominoIndex;
        }
    }

    public List<Domino> formChain(List<Domino> inputDominoes) throws ChainNotFoundException {
        if (inputDominoes.isEmpty()) {
            return new ArrayList<>();
        }

        if (inputDominoes.size() == 1) {
            Domino single = inputDominoes.get(0);
            if (single.getLeft() != single.getRight()) {
                throw new ChainNotFoundException("Single domino cannot form chain");
            }
            return new ArrayList<>(inputDominoes);
        }

        // Check degree parity
        Map<Integer, Integer> degrees = new HashMap<>();
        for (Domino d : inputDominoes) {
            degrees.put(d.getLeft(), degrees.getOrDefault(d.getLeft(), 0) + 1);
            degrees.put(d.getRight(), degrees.getOrDefault(d.getRight(), 0) + 1);
        }

        for (int count : degrees.values()) {
            if (count % 2 != 0) {
                throw new ChainNotFoundException("Odd degree detected");
            }
        }

        // Check connectedness
        Set<Integer> nodes = new HashSet<>();
        for (Domino d : inputDominoes) {
            nodes.add(d.getLeft());
            nodes.add(d.getRight());
        }

        if (!nodes.isEmpty()) {
            Map<Integer, Set<Integer>> adjNodes = new HashMap<>();
            for (Domino d : inputDominoes) {
                int u = d.getLeft();
                int v = d.getRight();
                adjNodes.computeIfAbsent(u, k -> new HashSet<>()).add(v);
                adjNodes.computeIfAbsent(v, k -> new HashSet<>()).add(u);
            }

            Set<Integer> visited = new HashSet<>();
            Queue<Integer> queue = new LinkedList<>();
            Integer start = nodes.iterator().next();
            queue.add(start);
            visited.add(start);
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v : adjNodes.getOrDefault(u, Collections.emptySet())) {
                    if (nodes.contains(v) && !visited.contains(v)) {
                        visited.add(v);
                        queue.add(v);
                    }
                }
            }

            if (!visited.equals(nodes)) {
                throw new ChainNotFoundException("Graph is not connected");
            }
        }

        // Build adjacency list with edges
        Map<Integer, List<Edge>> adjEdges = new HashMap<>();
        for (int i = 0; i < inputDominoes.size(); i++) {
            Domino d = inputDominoes.get(i);
            int u = d.getLeft();
            int v = d.getRight();
            adjEdges.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, i));
            adjEdges.computeIfAbsent(v, k -> new ArrayList<>()).add(new Edge(u, i));
        }

        // Find Eulerian circuit using Hierholzer's algorithm
        boolean[] used = new boolean[inputDominoes.size()];
        List<Integer> circuit = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        int startNode = inputDominoes.get(0).getLeft();
        stack.push(startNode);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            List<Edge> edges = adjEdges.getOrDefault(u, Collections.emptyList());
            boolean found = false;
            for (Edge edge : edges) {
                if (!used[edge.dominoIndex]) {
                    used[edge.dominoIndex] = true;
                    stack.push(u);
                    stack.push(edge.node);
                    found = true;
                    break;
                }
            }
            if (!found) {
                circuit.add(u);
            }
        }

        Collections.reverse(circuit);

        // Convert node circuit to domino chain
        List<Domino> chain = new ArrayList<>();
        for (int i = 0; i < circuit.size() - 1; i++) {
            int u = circuit.get(i);
            int v = circuit.get(i + 1);
            boolean edgeFound = false;
            for (Edge edge : adjEdges.getOrDefault(u, Collections.emptyList())) {
                if (edge.node == v && used[edge.dominoIndex]) {
                    Domino d = inputDominoes.get(edge.dominoIndex);
                    if (d.getLeft() == u && d.getRight() == v) {
                        chain.add(d);
                    } else {
                        chain.add(new Domino(v, u));
                    }
                    used[edge.dominoIndex] = false;
                    edgeFound = true;
                    break;
                }
            }
            if (!edgeFound) {
                throw new ChainNotFoundException("Failed to reconstruct chain");
            }
        }

        return chain;
    }
}
