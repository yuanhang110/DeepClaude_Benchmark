pub mod graph {
    use std::collections::HashMap;

    // Main Graph struct to hold all data
    pub struct Graph {
        pub nodes: HashMap<String, Node>,
        pub edges: Vec<Edge>,
        pub attrs: HashMap<String, String>,
    }

    // Node with id and attributes
    #[derive(Clone)]
    pub struct Node {
        pub id: String,
        pub attrs: HashMap<String, String>,
    }

    // Edge connects two nodes with attributes
    #[derive(Clone)]
    pub struct Edge {
        pub src: String,
        pub dst: String,
        pub attrs: HashMap<String, String>,
    }

    // GraphBuilder for constructing a graph
    pub struct GraphBuilder {
        nodes: HashMap<String, Node>,
        edges: Vec<Edge>,
        attrs: HashMap<String, String>,
        current_node: Option<String>,
        current_edge: Option<(String, String)>,
    }

    // NodeBuilder for fluent node attribute setting
    pub struct NodeBuilder<'a> {
        graph_builder: &'a mut GraphBuilder,
        node_id: String,
    }

    // EdgeBuilder for fluent edge attribute setting
    pub struct EdgeBuilder<'a> {
        graph_builder: &'a mut GraphBuilder,
        src: String,
        dst: String,
    }

    impl Graph {
        // Starting point for the builder pattern
        pub fn new() -> GraphBuilder {
            GraphBuilder {
                nodes: HashMap::new(),
                edges: Vec::new(),
                attrs: HashMap::new(),
                current_node: None,
                current_edge: None,
            }
        }
    }

    impl GraphBuilder {
        // Set a graph attribute
        pub fn graph_attr(&mut self, key: &str, value: &str) -> &mut Self {
            self.attrs.insert(key.to_string(), value.to_string());
            self
        }

        // Start building a node
        pub fn node(&mut self, id: &str) -> NodeBuilder {
            self.current_node = Some(id.to_string());
            
            // Create the node if it doesn't exist
            if !self.nodes.contains_key(id) {
                self.nodes.insert(id.to_string(), Node {
                    id: id.to_string(),
                    attrs: HashMap::new(),
                });
            }
            
            NodeBuilder {
                graph_builder: self,
                node_id: id.to_string(),
            }
        }

        // Start building an edge
        pub fn edge(&mut self, src: &str, dst: &str) -> EdgeBuilder {
            // Create nodes if they don't exist
            if !self.nodes.contains_key(src) {
                self.nodes.insert(src.to_string(), Node {
                    id: src.to_string(),
                    attrs: HashMap::new(),
                });
            }
            
            if !self.nodes.contains_key(dst) {
                self.nodes.insert(dst.to_string(), Node {
                    id: dst.to_string(),
                    attrs: HashMap::new(),
                });
            }
            
            self.current_edge = Some((src.to_string(), dst.to_string()));
            
            EdgeBuilder {
                graph_builder: self,
                src: src.to_string(),
                dst: dst.to_string(),
            }
        }

        // Finalize the graph construction
        pub fn build(&self) -> Graph {
            Graph {
                nodes: self.nodes.clone(),
                edges: self.edges.clone(),
                attrs: self.attrs.clone(),
            }
        }
    }

    impl<'a> NodeBuilder<'a> {
        // Set a node attribute
        pub fn attr(self, key: &str, value: &str) -> Self {
            if let Some(node) = self.graph_builder.nodes.get_mut(&self.node_id) {
                node.attrs.insert(key.to_string(), value.to_string());
            }
            self
        }

        // Allow chaining back to graph builder
        pub fn graph_attr(self, key: &str, value: &str) -> &'a mut GraphBuilder {
            self.graph_builder.graph_attr(key, value)
        }

        // Allow chaining to create another node
        pub fn node(self, id: &str) -> NodeBuilder<'a> {
            self.graph_builder.node(id)
        }

        // Allow chaining to create an edge
        pub fn edge(self, src: &str, dst: &str) -> EdgeBuilder<'a> {
            self.graph_builder.edge(src, dst)
        }
    }

    impl<'a> EdgeBuilder<'a> {
        // Set an edge attribute
        pub fn attr(self, key: &str, value: &str) -> Self {
            // Find if this edge already exists
            let edge_exists = self.graph_builder.edges.iter().position(|e| 
                e.src == self.src && e.dst == self.dst);
            
            if let Some(idx) = edge_exists {
                // Update existing edge
                self.graph_builder.edges[idx].attrs.insert(key.to_string(), value.to_string());
            } else {
                // Create new edge
                let mut attrs = HashMap::new();
                attrs.insert(key.to_string(), value.to_string());
                
                self.graph_builder.edges.push(Edge {
                    src: self.src.clone(),
                    dst: self.dst.clone(),
                    attrs,
                });
            }
            self
        }

        // Allow chaining back to graph builder
        pub fn graph_attr(self, key: &str, value: &str) -> &'a mut GraphBuilder {
            self.graph_builder.graph_attr(key, value)
        }

        // Allow chaining to create a node
        pub fn node(self, id: &str) -> NodeBuilder<'a> {
            self.graph_builder.node(id)
        }

        // Allow chaining to create another edge
        pub fn edge(self, src: &str, dst: &str) -> EdgeBuilder<'a> {
            self.graph_builder.edge(src, dst)
        }
    }
}
