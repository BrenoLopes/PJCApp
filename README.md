# PJCApp

## Como rodar
1. Copie o arquivo de .env.template ou .env.example e coloque as informações que queira (como tempo de expiração do token jwt)
2. Rode o docker-compose.yml com docker-compose up -d

## Como desenvolver
Este projeto foi compilado utilizando java 8 e node 14.15.4
1. Clone este repositório
2. Rode mvn spring-boot:run dentro da pasta backend
3. Rode ng serve dentro da pasta do frontend

## Como este projeto está dividido
Backend:
1. config - Arquivos de configuração de token jwt, cors, etc
2. controllers - Controladores de api e de redirecionamento de outras rotas para o app angular
3. dto - DTOs utilizados na aplicação
4. entity - Entidades utilizadas na aplicação
5. repository - Repositorios utilizados na aplicação
6. services - Serviços utilizados como Token, Barreira de proteção do app contra db, etc.
7. utilities - Classes utilitárias

Frontend
1. core - Servições necessários para comunicar com o bd ou com o local storage
2. materialdesign - Módulo para facilitar a utilização do material design
3. pages - Componentes controladores
4. shared - Componentes livres, como dialogs.
