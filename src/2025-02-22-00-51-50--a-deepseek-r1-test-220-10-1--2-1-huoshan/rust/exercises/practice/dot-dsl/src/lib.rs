pub mod graph {
    pub struct Graph {
        pub nodes: std::collections::HashMap<String, NodeAttributes>,
        pub edges: Vec<(String, String, EdgeAttributes)>,
        pub attrs: std::collections::HashMap<String, String>,
    }

    pub struct GraphBuilder {
        pub nodes: std::collections::HashMap<String, NodeAttributes>,
        pub edges: Vec<(String, String, EdgeAttributes)>,
        pub attrs: std::collections::HashMap<String, String>,
    }

    pub mod graph_items {
        pub mod node {
            use super::super::NodeAttributes;
            
            #[derive(Debug, Clone, PartialEq)]
            pub struct Node {
                pub id: String,
                pub attributes: NodeAttributes,
            }

            impl Node {
                pub fn new(id: &str) -> Self {
                    Node {
                        id: id.to_string(),
                        attributes: NodeAttributes::new(),
                    }
                }

                pub fn with_attributes(mut self, attributes: NodeAttributes) -> Self {
                    self.attributes = attributes;
                    self
                }
            }
        }

        pub mod edge {
            use super::super::EdgeAttributes;
            
            #[derive(Debug, Clone, PartialEq)]
            pub struct Edge {
                pub from: String,
                pub to: String,
                pub attributes: EdgeAttributes,
            }

            impl Edge {
                pub fn new(from: &str, to: &str) -> Self {
                    Edge {
                        from: from.to_string(),
                        to: to.to_string(),
                        attributes: EdgeAttributes::new(),
                    }
                }

                pub fn with_attributes(mut self, attributes: EdgeAttributes) -> Self {
                    self.attributes = attributes;
                    self
                }
            }
        }
    }

    #[derive(Default)]
    pub struct NodeAttributes {
        pub color: Option<String>,
        pub background_color: Option<String>,
    }

    #[derive(Default)]
    pub struct EdgeAttributes {
        pub color: Option<String>,
        pub style: Option<String>,
    }

    impl Graph {
        pub fn new() -> GraphBuilder {
            GraphBuilder::new()
        }
    }

    impl GraphBuilder {
        pub fn new() -> Self {
            Self {
                nodes: std::collections::HashMap::new(),
                edges: Vec::new(),
                attrs: std::collections::HashMap::new(),
            }
        }

        pub fn with_nodes(mut self, nodes: &[graph_items::node::Node]) -> Self {
            for node in nodes {
                self.nodes.insert(node.id.clone(), node.attributes.clone());
            }
            self
        }

        pub fn with_edges(mut self, edges: &[graph_items::edge::Edge]) -> Self {
            for edge in edges {
                self.edges.push((edge.from.clone(), edge.to.clone(), edge.attributes.clone()));
            }
            self
        }

        pub fn with_attrs(mut self, attrs: &[(&str, &str)]) -> Self {
            for (k, v) in attrs {
                self.attrs.insert(k.to_string(), v.to_string());
            }
            self
        }

        pub fn build(self) -> Graph {
            Graph {
                nodes: self.nodes,
                edges: self.edges,
            }
        }
    }

    impl NodeAttributes {
        pub fn new() -> Self {
            Self::default()
        }

        pub fn color(mut self, color: &str) -> Self {
            self.color = Some(color.to_string());
            self
        }

        pub fn background_color(mut self, bg_color: &str) -> Self {
            self.background_color = Some(bg_color.to_string());
            self
        }
    }

    impl EdgeAttributes {
        pub fn new() -> Self {
            Self::default()
        }

        pub fn color(mut self, color: &str) -> Self {
            self.color = Some(color.to_string());
            self
        }

        pub fn style(mut self, style: &str) -> Self {
            self.style = Some(style.to_string());
            self
        }
    }
}
