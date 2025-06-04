# CC-Chat 🤖🧠  
一个基于 Spring Boot + 通义千问（Qwen）大模型的 AI 聊天项目，支持用户注册登录、JWT 认证、上下文记忆、Redis 缓存与 MySQL 持久化，附带简单前端页面。

---

## 🔧 技术栈  
- Java 17  
- Spring Boot 3  
- Spring Security + JWT  
- Redis（对话上下文存储）  
- MySQL（用户信息与聊天记录持久化）  
- 通义千问 Qwen API  
- OkHttp + Jackson  
- 前端：原生 HTML + JS  

---

## ✨ 功能特点  
- ✅ 用户注册 / 登录（密码加密）  
- ✅ JWT 登录认证，保护接口安全  
- ✅ 用户对话上下文自动记忆（Redis）  
- ✅ 聊天记录自动存入 MySQL  
- ✅ 登录后自动携带上下文连续对话  
- ✅ 支持切换账号，互不干扰  
- ✅ 简洁易用的 Web 聊天界面  

---

## 🚀 使用方式  

### 1. 克隆项目  
```bash
git clone https://github.com/Explainfuture/CC-Chat.git
cd CC-Chat
