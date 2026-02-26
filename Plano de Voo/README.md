# ✈️ Plano de Voo

Sistema de submissão de planos de voo desenvolvido em **Java Swing** com integração a banco de dados **MySQL**. O projeto oferece uma interface gráfica intuitiva para que pilotos ou despachantes possam cadastrar e validar informações de voo conforme os padrões aeronáuticos.

## 🚀 Funcionalidades

- **Interface Gráfica (GUI)**: Formulário completo para inserção de dados do plano de voo.
- **Validação de Dados**: Verificação em tempo real de formatos (Horários, Identificações, Números).
- **Persistência de Dados**: Armazenamento seguro de cada plano de voo em banco de dados MySQL.
- **Campos Detalhados**:
  - Identificação e Tipo de Aeronave.
  - Regras de Voo (I, V, Y, Z).
  - Categoria de Esteira de Turbulência.
  - Equipamentos e Vigilância.
  - Aeródromos de Partida, Destino e Alternativa.
  - Rota, Velocidade e Nível de Cruzeiro.

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Java (JDK 17+)
- **Interface**: Swing (AWT)
- **Banco de Dados**: MySQL 8.0
- **Conectividade**: JDBC (Java Database Connectivity)

## 📋 Pré-requisitos

Antes de começar, você precisará ter instalado em sua máquina:
- [Java JDK](https://www.oracle.com/java/technologies/downloads/) (Versão 17 ou superior recomendada).
- [MySQL Server](https://dev.mysql.com/downloads/installer/).
- Driver JDBC do MySQL (`mysql-connector-java`).

## 🔧 Configuração e Instalação

### 1. Banco de Dados
Crie o banco de dados e a tabela necessária executando o seguinte script SQL no seu terminal MySQL ou Workbench:

```sql
CREATE DATABASE IF NOT EXISTS exemplobd;
USE exemplobd;

CREATE TABLE IF NOT EXISTS plano_voo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    Identificação_da_Aeronave VARCHAR(50),
    Regras VARCHAR(50),
    Tipo_de_Voo VARCHAR(10),
    Número_de_Aeronave VARCHAR(10),
    Tipo_de_Aeronave VARCHAR(50),
    Catet VARCHAR(10),
    Equipamento VARCHAR(50),
    Equipamento_Vigilancia VARCHAR(50),
    Aerodromo_PT VARCHAR(10),
    Hora_EOB VARCHAR(5),
    Velocidade VARCHAR(20),
    Nível_de_Voo VARCHAR(20),
    Rota TEXT,
    Aerodromo_Destino VARCHAR(10),
    Duração_Total_do_Voo VARCHAR(10),
    Aerodromo_Alternativa VARCHAR(10),
    Observacoes TEXT
);
```

### 2. Configuração do Código
O código está configurado para conectar ao MySQL local:
- **URL**: `jdbc:mysql://localhost:3306/exemplobd`
- **Usuário**: `root`
- **Senha**: `root`

> [!IMPORTANT]
> Se suas credenciais do MySQL forem diferentes, atualize a linha 271 no arquivo `PlanoDeVooApp.java`.

## 🏃 Como Executar

1. Compile o projeto (certifique-se de que o Driver JDBC está no classpath):
   ```bash
   javac -cp ".;lib/mysql-connector-java.jar" src/PlanoDeVooApp.java
   ```
2. Execute a aplicação:
   ```bash
   java -cp ".;lib/mysql-connector-java.jar;src" PlanoDeVooApp
   ```

## 🤝 Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir uma *Issue* ou enviar um *Pull Request*.

---
Desenvolvido por João Pedro.
