pub mod graph {
    use std::collections::HashMap;

    pub struct Graph {
        nodes: HashMap<String, Node>,
        edges: Vec<Edge>,
        graph_attrs: HashMap<String, String>,
    }

    pub struct Node {
        attrs: HashMap<String, String>,
    }

    pub struct Edge {
        from: String,
        to: String,
        attrs: HashMap<String, String>,
    }

    pub struct GraphBuilder {
        nodes: HashMap<String, Node>,
        edges: Vec<Edge>,
        graph_attrs: HashMap<String, String>,
    }

    pub struct NodeBuilder {
        attrs: HashMap<String, String>,
    }

    pub struct EdgeBuilder {
        from: String,
        to: String,
        attrs: HashMap<String, String>,
    }

    impl Graph {
        pub fn new() -> GraphBuilder {
            GraphBuilder {
                nodes: HashMap::new(),
                edges: Vec::new(),
                graph_attrs: HashMap::new(),
            }
        }
    }

    impl GraphBuilder {
        pub fn add_node(mut self, id: &str, f: impl FnOnce(NodeBuilder) -> NodeBuilder) -> Self {
            let node_builder = NodeBuilder::new();
            let node_builder = f(node_builder);
            let node = node_builder.build();
            self.nodes.insert(id.to_string(), node);
            self
        }

        pub fn add_edge(mut self, from: &str, to: &str, f: impl FnOnce(EdgeBuilder) -> EdgeBuilder) -> Self {
            let edge_builder = EdgeBuilder::new(from, to);
            let edge_builder = f(edge_builder);
            let edge = edge_builder.build();
            self.edges.push(edge);
            self
        }

        pub fn graph_attr(mut self, name: &str, value: &str) -> Self {
            self.graph_attrs.insert(name.to_string(), value.to_string());
            self
        }

        pub fn build(self) -> Graph {
            Graph {
                nodes: self.nodes,
                edges: self.edges,
                graph_attrs: self.graph_attrs,
            }
        }
    }

    impl NodeBuilder {
        pub fn new() -> Self {
            Self {
                attrs: HashMap::new(),
            }
        }

        pub fn attr(mut self, name: &str, value: &str) -> Self {
            self.attrs.insert(name.to_string(), value.to_string());
            self
        }

        pub fn build(self) -> Node {
            Node { attrs: self.attrs }
        }
    }

    impl EdgeBuilder {
        pub fn new(from: &str, to: &str) -> Self {
            Self {
                from: from.to_string(),
                to: to.to_string(),
                attrs: HashMap::new(),
            }
        }

        pub fn attr(mut self, name: &str, value: &str) -> Self {
            self.attrs.insert(name.to_string(), value.to_string());
            self
        }

        pub fn build(self) -> Edge {
            Edge {
                from: self.from,
                to: self.to,
                attrs: self.attrs,
            }
        }
    }
}
