# ✈️ Sistema de Plano de Voo (Híbrido)

Este é um ecossistema moderno para submissão de Planos de Voo (ICAO FPL), integrando uma aplicação desktop legado (Java) com uma interface web moderna e responsiva.

## 🌟 Principais Características
- **Arquitetura Híbrida**: Utilize a aplicação `.exe` local ou acesse via navegador de qualquer lugar.
- **Sincronização em Nuvem**: Ambos os sistemas utilizam o banco de dados **Neon (PostgreSQL)** em tempo real.
- **Design Premium**: Interface web com Glassmorphism, otimizada para Desktop e Mobile.
- **Serverless**: Backend da web processado via **Netlify Functions**.

---

## 📁 Estrutura do Projeto

```text
├── Plano de Voo/        # Código fonte da aplicação Desktop (Java Swing)
├── web_frontend/        # Código fonte da aplicação Web
│   ├── netlify/         # Funções Serverless (Node.js)
│   ├── index.html       # Interface principal
│   ├── styles.css       # Estilização Premium
│   └── app.js           # Lógica do front-end
└── README.md            # Esta documentação
```

---

## ⚙️ Configuração e Instalação

### 1. Banco de Dados (Neon)
1. Crie uma conta no [Neon.tech](https://neon.tech).
2. Execute o script `web_frontend/schema.sql` no console SQL do Neon para criar a tabela necessária.

### 2. Configuração Web (Netlify)
1. Faça o upload da pasta `web_frontend` para o Netlify.
2. Nas configurações do site, adicione a variável de ambiente:
   - `DATABASE_URL`: Sua Connection String completa do Neon.

### 3. Configuração Desktop (Java)
1. Certifique-se de ter o [JDBC Driver do PostgreSQL](https://jdbc.postgresql.org/download/) no seu Classpath.
2. No arquivo `PlanoDeVooApp.java`, as credenciais do banco devem ser preenchidas conforme sua instância no Neon.

---

## 📱 Responsividade Mobile
A versão web foi otimizada para dispositivos móveis, apresentando:
- Empilhamento inteligente de campos.
- Botões de toque facilitado.
- Feedback visual via Toasts (notificações).

---

## 🛠️ Tecnologias Utilizadas
- **Desktop**: Java (Swing), JDBC.
- **Web**: HTML5, CSS3 (Vanilla), JavaScript (ES6).
- **Backend**: Node.js, Netlify Functions.
- **Banco de Dados**: PostgreSQL (Neon Cloud).

---
*Desenvolvido como um projeto de modernização de sistemas legados.*
