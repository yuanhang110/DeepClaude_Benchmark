pub mod graph {
    use std::collections::HashMap;

    pub mod graph_items {
        pub mod node {
            use std::collections::HashMap;
            
            #[derive(Debug, Clone)]
            pub struct Node {
                pub id: String,
                pub attributes: HashMap<String, String>,
            }
        }
        
        pub mod edge {
            use std::collections::HashMap;
            
            #[derive(Debug, Clone)]
            pub struct Edge {
                pub from: String,
                pub to: String,
                pub attributes: HashMap<String, String>,
            }
        }
    }

    #[derive(Debug)]
    pub struct Graph {
        pub nodes: Vec<graph_items::node::Node>,
        pub edges: Vec<graph_items::edge::Edge>,
        pub attrs: HashMap<String, String>,
    }

    impl Graph {
        pub fn new() -> Self {
            Graph {
                nodes: Vec::new(),
                edges: Vec::new(),
                attrs: HashMap::new(),
            }
        }

        pub fn with_nodes(mut self, nodes: &[graph_items::node::Node]) -> Self {
            self.nodes.extend_from_slice(nodes);
            self
        }

        pub fn with_edges(mut self, edges: &[graph_items::edge::Edge]) -> Self {
            self.edges.extend_from_slice(edges);
            self
        }

        pub fn with_attrs(mut self, attrs: &[(&str, &str)]) -> Self {
            for (key, value) in attrs {
                self.attrs.insert(key.to_string(), value.to_string());
            }
            self
        }

        pub fn node(&mut self, id: &str) -> NodeBuilder {
            NodeBuilder::new(self, id.to_string())
        }

        pub fn edge(&mut self, from: &str, to: &str) -> EdgeBuilder {
            EdgeBuilder::new(self, from.to_string(), to.to_string())
        }
    }

    pub struct NodeBuilder<'a> {
        graph: &'a mut Graph,
        node: graph_items::node::Node,
    }

    impl<'a> NodeBuilder<'a> {
        fn new(graph: &'a mut Graph, id: String) -> Self {
            NodeBuilder {
                graph,
                node: graph_items::node::Node {
                    id,
                    attributes: HashMap::new(),
                },
            }
        }

        pub fn attr(mut self, key: &str, value: &str) -> Self {
            self.node.attributes.insert(key.to_string(), value.to_string());
            self
        }

        pub fn build(self) {
            self.graph.nodes.push(self.node);
        }
    }

    pub struct EdgeBuilder<'a> {
        graph: &'a mut Graph,
        edge: graph_items::edge::Edge,
    }

    impl<'a> EdgeBuilder<'a> {
        fn new(graph: &'a mut Graph, from: String, to: String) -> Self {
            EdgeBuilder {
                graph,
                edge: graph_items::edge::Edge {
                    from,
                    to,
                    attributes: HashMap::new(),
                },
            }
        }

        pub fn attr(mut self, key: &str, value: &str) -> Self {
            self.edge.attributes.insert(key.to_string(), value.to_string());
            self
        }

        pub fn build(self) {
            self.graph.edges.push(self.edge);
        }
    }
}
