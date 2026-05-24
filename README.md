API backend para gestão de alunos, grupos, tarefas acadêmicas e notificações via WhatsApp.

## Base URL

```http
http://localhost:8080
````


## Autenticação

A API utiliza autenticação JWT armazenada em cookie HttpOnly.

Após o login, o backend retorna automaticamente um cookie seguro contendo o token JWT.

O frontend deve enviar as requisições com:

```javascript
credentials: "include"
````

Exemplo:

```javascript
fetch("http://localhost:8080/api/students", {
  method: "GET",
  credentials: "include"
});
```

## Endpoints públicos

Os endpoints abaixo não exigem autenticação:

```http
POST /api/auth/register
POST /api/auth/login
POST /api/whatsapp/webhook
```

Todos os demais endpoints exigem autenticação via cookie JWT HttpOnly.

# Auth

## Registrar usuário

```http
POST /api/auth/register
```

## Login

```http
POST /api/auth/login
```

---

# Students

## Criar aluno

```http
POST /api/students
```

```json
{
  "name": "Breno Carvalho",
  "registrationNumber": "2026001",
  "phoneNumber": "+5534999999999",
  "birthDate": "2002-05-10"
}
```

## Listar alunos

```http
GET /api/students
```

## Buscar aluno por ID

```http
GET /api/students/{id}
```

## Atualizar aluno

```http
PUT /api/students/{id}
```

## Deletar aluno

```http
DELETE /api/students/{id}
```

---

# Groups

## Criar grupo

```http
POST /api/groups
```

```json
{
  "name": "Engenharia de Software - 5º Período",
  "description": "Turma principal do curso"
}
```

## Listar grupos

```http
GET /api/groups
```

## Buscar grupo por ID

```http
GET /api/groups/{id}
```

## Atualizar grupo

```http
PUT /api/groups/{id}
```

## Deletar grupo

```http
DELETE /api/groups/{id}
```

---

# Student Groups

## Vincular aluno a grupo

```http
POST /api/student-groups
```

```json
{
  "studentId": 1,
  "groupId": 1
}
```

## Listar vínculos

```http
GET /api/student-groups
```

## Buscar vínculo por ID

```http
GET /api/student-groups/{id}
```

## Listar grupos de um aluno

```http
GET /api/student-groups/student/{studentId}
```

## Listar alunos de um grupo

```http
GET /api/student-groups/group/{groupId}
```

## Remover vínculo

```http
DELETE /api/student-groups/{id}
```

---

# Academic Tasks

## Criar tarefa acadêmica

```http
POST /api/academic-tasks
```

```json
{
  "title": "Prova de Cálculo",
  "description": "Prova sobre limites e derivadas na sala B204.",
  "type": "EXAM",
  "eventDate": "2026-05-25T08:00:00",
  "notificationDate": "2026-05-24T08:00:00",
  "groupId": 1
}
```

## Tipos disponíveis

```text
EXAM
ASSIGNMENT
NOTICE
EVENT
ROOM_CHANGE
```

## Listar tarefas

```http
GET /api/academic-tasks
```

## Buscar tarefa por ID

```http
GET /api/academic-tasks/{id}
```

## Listar tarefas por grupo

```http
GET /api/academic-tasks/group/{groupId}
```

## Listar tarefas por tipo

```http
GET /api/academic-tasks/type/{type}
```

## Atualizar tarefa

```http
PUT /api/academic-tasks/{id}
```

## Deletar tarefa

```http
DELETE /api/academic-tasks/{id}
```

---

# Fluxo automático de notificações

## Gerar notificações para uma tarefa

Busca todos os alunos do grupo da tarefa e cria uma notificação para cada aluno.

```http
POST /api/academic-tasks/{id}/generate-notifications
```

## Enviar notificações da tarefa

Envia pelo WhatsApp todas as notificações já geradas para a tarefa.

```http
POST /api/academic-tasks/{id}/send-notifications
```

## Gerar e enviar notificações

```http
POST /api/academic-tasks/{id}/generate-and-send-notifications
```

---

# Notifications

## Criar notificação manual

```http
POST /api/notifications
```

```json
{
  "taskId": 1,
  "studentId": 1,
  "message": "📚 Prova de Cálculo amanhã às 08:00 na sala B204."
}
```

## Listar notificações

```http
GET /api/notifications
```

## Buscar notificação por ID

```http
GET /api/notifications/{id}
```

## Listar notificações por aluno

```http
GET /api/notifications/student/{studentId}
```

## Listar notificações por tarefa

```http
GET /api/notifications/task/{taskId}
```

## Listar notificações por status

```http
GET /api/notifications/status/{status}
```

## Status disponíveis

```text
PENDING
SENT
ERROR
```

## Enviar notificação pelo WhatsApp

```http
POST /api/notifications/{id}/send
```

## Atualizar notificação

```http
PUT /api/notifications/{id}
```

## Deletar notificação

```http
DELETE /api/notifications/{id}
```

---

# WhatsApp

## Enviar mensagem manual

```http
POST /api/whatsapp/send
```

```json
{
  "to": "+5534999999999",
  "message": "📚 Prova de Cálculo amanhã às 08:00 na sala B204."
}
```

## Webhook para receber mensagens da Twilio

```http
POST /api/whatsapp/webhook
```

Esse endpoint deve ser cadastrado na Twilio.

---

# Jobs automáticos

## Aniversário

Executa diariamente e envia mensagem de feliz aniversário para alunos aniversariantes.

## Lembrete de tarefas próximas

Executa diariamente e envia lembretes para tarefas com data final em até 3 dias.

## Resumo semanal

Executa semanalmente e envia um resumo geral das tarefas abertas.

---

# Erros comuns

## 404 - Recurso não encontrado

```json
{
  "status": 404,
  "error": "Student not found",
  "message": "Student not found with id: 1"
}
```

## 409 - Duplicidade

```json
{
  "status": 409,
  "error": "Registration number already exists",
  "message": "Registration number already exists: 2026001"
}
```

## 400 - Validação

```json
{
  "status": 400,
  "error": "Validation error",
  "message": "must not be blank"
}
```

```
```
