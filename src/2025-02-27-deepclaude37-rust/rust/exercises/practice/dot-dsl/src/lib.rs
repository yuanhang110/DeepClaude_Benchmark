pub mod graph {
    use std::collections::{HashMap, HashSet};

    pub mod graph_items {
        pub mod node {
            use std::collections::HashMap;

            #[derive(Clone, Debug, PartialEq)]
            pub struct Node {
                pub id: String,
                pub attrs: HashMap<String, String>,
            }

            impl Node {
                pub fn new(id: &str) -> Self {
                    Self {
                        id: id.to_string(),
                        attrs: HashMap::new(),
                    }
                }

                pub fn with_attrs(mut self, attrs: &[(&str, &str)]) -> Self {
                    for &(k, v) in attrs {
                        self.attrs.insert(k.to_string(), v.to_string());
                    }
                    self
                }

                pub fn get_attr(&self, key: &str) -> Option<&str> {
                    self.attrs.get(key).map(|s| s.as_str())
                }
            }
        }

        pub mod edge {
            use std::collections::HashMap;

            #[derive(Clone, Debug, PartialEq)]
            pub struct Edge {
                pub from: String,
                pub to: String,
                pub attrs: HashMap<String, String>,
            }

            impl Edge {
                pub fn new(from: &str, to: &str) -> Self {
                    Self {
                        from: from.to_string(),
                        to: to.to_string(),
                        attrs: HashMap::new(),
                    }
                }

                pub fn with_attrs(mut self, attrs: &[(&str, &str)]) -> Self {
                    for &(k, v) in attrs {
                        self.attrs.insert(k.to_string(), v.to_string());
                    }
                    self
                }
            }
        }
    }

    use graph_items::edge::Edge;
    use graph_items::node::Node;

    pub struct Graph {
        pub nodes: HashSet<String>,
        pub edges: HashSet<(String, String)>,
        node_attrs: HashMap<String, HashMap<String, String>>,
        edge_attrs: HashMap<(String, String), HashMap<String, String>>,
        pub attrs: HashMap<String, String>,
    }

    impl Graph {
        pub fn new() -> Self {
            Self {
                nodes: HashSet::new(),
                edges: HashSet::new(),
                node_attrs: HashMap::new(),
                edge_attrs: HashMap::new(),
                attrs: HashMap::new(),
            }
        }

        // Add a node to the graph
        fn add_node(mut self, node_id: &str) -> Self {
            self.nodes.insert(node_id.to_string());
            if !self.node_attrs.contains_key(node_id) {
                self.node_attrs.insert(node_id.to_string(), HashMap::new());
            }
            self
        }

        // Add an edge between two nodes
        fn add_edge(mut self, from: &str, to: &str) -> Self {
            // Ensure both nodes exist
            self = self.add_node(from).add_node(to);
            
            // Add the edge
            let edge = (from.to_string(), to.to_string());
            self.edges.insert(edge.clone());
            
            // Initialize edge attributes if needed
            if !self.edge_attrs.contains_key(&edge) {
                self.edge_attrs.insert(edge, HashMap::new());
            }
            
            self
        }

        // Add multiple nodes at once
        pub fn with_nodes(mut self, nodes: &[Node]) -> Self {
            for node in nodes {
                self = self.add_node(&node.id);
                
                // Add node attributes
                for (key, value) in &node.attrs {
                    self.node_attrs
                        .get_mut(&node.id)
                        .unwrap()
                        .insert(key.clone(), value.clone());
                }
            }
            self
        }

        // Add multiple edges at once
        pub fn with_edges(mut self, edges: &[Edge]) -> Self {
            for edge in edges {
                self = self.add_edge(&edge.from, &edge.to);
                
                // Add edge attributes
                let edge_key = (edge.from.clone(), edge.to.clone());
                for (key, value) in &edge.attrs {
                    self.edge_attrs
                        .get_mut(&edge_key)
                        .unwrap()
                        .insert(key.clone(), value.clone());
                }
            }
            self
        }

        // Set an attribute for a node
        pub fn with_node_attr(mut self, node_id: &str, key: &str, value: &str) -> Self {
            // Ensure the node exists
            self = self.add_node(node_id);
            
            // Set the attribute
            self.node_attrs
                .get_mut(&node_id.to_string())
                .unwrap()
                .insert(key.to_string(), value.to_string());
            
            self
        }

        // Set an attribute for an edge
        pub fn with_edge_attr(mut self, from: &str, to: &str, key: &str, value: &str) -> Self {
            // Ensure the edge exists
            self = self.add_edge(from, to);
            
            // Set the attribute
            let edge = (from.to_string(), to.to_string());
            self.edge_attrs
                .get_mut(&edge)
                .unwrap()
                .insert(key.to_string(), value.to_string());
            
            self
        }

        // Set multiple graph-level attributes
        pub fn with_attrs(mut self, attrs: &[(&str, &str)]) -> Self {
            for &(key, value) in attrs {
                self.attrs.insert(key.to_string(), value.to_string());
            }
            self
        }

        // Set a single graph-level attribute
        pub fn with_graph_attr(mut self, key: &str, value: &str) -> Self {
            self.attrs.insert(key.to_string(), value.to_string());
            self
        }

        // Get a node by ID
        pub fn get_node(&self, id: &str) -> Option<Node> {
            if !self.nodes.contains(id) {
                return None;
            }
            
            let mut attrs = HashMap::new();
            if let Some(node_attrs) = self.node_attrs.get(id) {
                for (k, v) in node_attrs {
                    attrs.insert(k.clone(), v.clone());
                }
            }
            
            Some(Node {
                id: id.to_string(),
                attrs,
            })
        }

        // Get an edge by source and target
        pub fn get_edge(&self, from: &str, to: &str) -> Option<Edge> {
            let edge_key = (from.to_string(), to.to_string());
            if !self.edges.contains(&edge_key) {
                return None;
            }
            
            let mut attrs = HashMap::new();
            if let Some(edge_attrs) = self.edge_attrs.get(&edge_key) {
                for (k, v) in edge_attrs {
                    attrs.insert(k.clone(), v.clone());
                }
            }
            
            Some(Edge {
                from: from.to_string(),
                to: to.to_string(),
                attrs,
            })
        }
    }
}
