<div align="center">

<h1>DeepClaude_Benchmark 🐬🧠</h1>

<img src="frontend/public/deepclaude.png" width="300">

Harness the power of DeepSeek R1's reasoning and Claude's creativity and code generation capabilities with a unified API and chat interface.

[![GitHub license](https://img.shields.io/github/license/getasterisk/deepclaude)](https://github.com/getasterisk/deepclaude/blob/main/LICENSE.md)
[![Rust](https://img.shields.io/badge/rust-v1.75%2B-orange)](https://www.rust-lang.org/)
[![API Status](https://img.shields.io/badge/API-Stable-green)](https://deepclaude.asterisk.so)

[Getting Started](#getting-started) •
[Features](#features) •
[API Usage](#api-usage) •
[Documentation](#documentation) •
[Self-Hosting](#self-hosting) •
[Contributing](#contributing)

</div>

DeepClaude是一款高性能的大语言模型推理应用程序编程接口（API），它将DeepSeek R1的思维链（CoT）推理能力与Anthropic Claude的创意和代码生成能力相结合。它提供了一个统一的接口，以便在充分利用这两种模型优势的同时，还能让用户完全掌控自己的API密钥和数据。

DeepClaude具备以下特点：
1. **零延迟**：由高性能的Rust API提供支持，先由R1的思维链推理，随后Claude做出响应，在单个流中即可实现即时回复。
2. **私密且安全**：具备端到端的安全性，可进行本地API密钥管理，确保用户数据的隐私性。
3. **高度可配置**：能够根据用户需求对API和接口的各个方面进行自定义设置。
4. **开源**：拥有免费且开源的代码库，用户可以根据自身意愿进行贡献、修改和部署。
5. **双重人工智能能力**：将DeepSeek R1的推理能力与Claude的创造力和代码生成能力相结合。
6. **自带密钥管理的API**：用户可以在DeepClaude的托管基础设施中使用自己的API密钥，从而实现完全的控制权。

选择将R1和Claude结合的原因如下：
DeepSeek R1的思维链推理展示了其深度推理能力，大语言模型甚至能达到“元认知”的程度，比如自我纠正、思考极端情况，以及使用自然语言进行准蒙特卡洛树搜索。

然而，R1在代码生成、创造力和对话技巧方面存在不足。而Claude 3.5 Sonnet在这些领域表现出色，恰好能完美地弥补R1的不足。因此，DeepClaude将这两种模型结合起来，能够提供：
- R1卓越的推理和问题解决能力。
- Claude出色的代码生成能力和创造力。
- 只需一次API调用，即可实现快速的流式响应。
- 用户可通过自己的API密钥实现完全的控制权。 
