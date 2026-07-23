# Planejamento do Projeto: Kanban SaaS Multi-Tenant

## Visao Geral

Este projeto tem dois objetivos principais:

1. Aprendizado pratico de arquitetura backend, frontend, seguranca, multi-tenancy, cache, observabilidade e deploy.
2. Portfolio tecnico, demonstrando uma aplicacao SaaS realista, bem estruturada e com decisoes arquiteturais explicitas.

A ideia e construir um kanban multi-tenant no estilo SaaS, onde diferentes empresas ou times possam criar seus proprios workspaces, convidar membros, gerenciar boards e mover tarefas entre colunas com isolamento completo de dados entre tenants.

O foco nao deve ser criar um clone completo do Trello. O foco deve ser entregar um produto pequeno, coeso e tecnicamente forte.

## Objetivo do Produto

Permitir que usuarios criem ou participem de workspaces independentes, onde cada workspace possui seus proprios boards, colunas, tarefas, membros e permissoes.

Exemplo de fluxo principal:

1. Usuario cria uma conta.
2. Usuario cria um workspace.
3. Usuario cria um board dentro do workspace.
4. Usuario cria colunas como `Todo`, `In Progress` e `Done`.
5. Usuario cria tarefas e move essas tarefas entre colunas.
6. Usuario convida outros membros para o workspace.
7. Cada membro acessa apenas os dados dos workspaces dos quais faz parte.

## Stack Sugerida

- Frontend: React + TypeScript
- Backend: Java + Spring Boot
- Banco de dados: PostgreSQL
- Cache e apoio de sessao: Redis
- Observabilidade: Spring Boot Actuator + Micrometer + Prometheus + Grafana
- Ambiente local: Docker Compose
- CI/CD: GitHub Actions
- Deploy: Render, Railway, Fly.io, VPS ou outro provedor simples

## Escopo Principal

### Autenticacao e Seguranca

- Registro de usuarios.
- Login.
- Access token com JWT.
- Refresh token seguro, preferencialmente em cookie `HttpOnly`.
- Logout.
- Hash seguro de senha com BCrypt ou Argon2.
- Reset de senha.
- Verificacao de email, se o escopo permitir.
- Rate limit em endpoints sensiveis como login e reset de senha.

### Multi-Tenancy

- Cada workspace representa um tenant.
- Usuarios podem pertencer a varios workspaces.
- Quase todas as entidades principais devem carregar `workspace_id`.
- Toda query sensivel deve validar o workspace atual.
- O frontend nunca deve ser fonte de verdade para autorizacao.
- O backend deve garantir que um usuario nao consiga acessar dados de outro workspace.

### Workspaces e Membros

- Criar workspace.
- Listar workspaces do usuario.
- Atualizar dados basicos do workspace.
- Convidar membros.
- Aceitar convite.
- Remover membros.
- Alterar papel de membros.

### Permissoes

Papeis sugeridos:

- `OWNER`: dono do workspace, pode gerenciar tudo.
- `ADMIN`: pode gerenciar boards, tarefas e membros, mas nao excluir o workspace se essa regra for desejada.
- `MEMBER`: pode criar, editar e mover tarefas.
- `VIEWER`: pode visualizar boards e tarefas, mas nao pode alterar.

O sistema deve ter testes tentando acessar recursos sem permissao e recursos de outros workspaces.

### Boards, Colunas e Tarefas

- Criar board dentro de um workspace.
- Listar boards de um workspace.
- Criar colunas dentro de um board.
- Ordenar colunas.
- Criar tarefas.
- Editar tarefas.
- Mover tarefas entre colunas.
- Reordenar tarefas dentro de uma coluna.
- Arquivar ou excluir tarefas.

### Audit Log e Activity Feed

Essa parte e excelente para portfolio porque mostra rastreabilidade e modelagem de eventos.

Eventos sugeridos:

- Usuario criou uma tarefa.
- Usuario moveu uma tarefa de uma coluna para outra.
- Usuario alterou o titulo de uma tarefa.
- Usuario criou um board.
- Usuario convidou outro membro.
- Usuario alterou o papel de um membro.

Exemplo:

```text
Samuel moveu "Criar endpoint de login" de Todo para In Progress.
```

## Papel do Redis

Redis deve entrar como parte real da arquitetura, nao apenas como uma ferramenta adicionada por aparencia.

Usos recomendados:

### Cache de Boards

Cachear a visualizacao principal de um board, incluindo colunas e tarefas.

Exemplo:

```text
board:view:{workspaceId}:{boardId}
```

Ao criar, editar, mover ou excluir uma tarefa, o cache correspondente deve ser invalidado.

### Rate Limiting

Usar Redis para limitar tentativas em endpoints sensiveis:

- Login.
- Reset de senha.
- Reenvio de email de verificacao.
- Aceite de convite, se necessario.

Exemplo:

```text
rate-limit:login:{ip}
rate-limit:login:{email}
```

### Sessao e Refresh Tokens

Redis pode armazenar metadados de sessao ou denylist de tokens revogados.

Exemplos:

```text
session:{userId}:{sessionId}
token-denylist:{tokenId}
```

### Cache de Permissoes

Permissoes de usuario por workspace podem ser cacheadas para reduzir consultas repetidas.

Exemplo:

```text
permissions:{workspaceId}:{userId}
```

## Papel do Prometheus e Grafana

Prometheus faz sentido no projeto, mas deve entrar depois que a aplicacao ja tiver funcionalidades suficientes para serem observadas.

Ele deve fazer parte da fase de observabilidade, junto com Grafana.

Stack recomendada:

- Spring Boot Actuator.
- Micrometer.
- Micrometer Prometheus Registry.
- Prometheus coletando metricas da API.
- Grafana exibindo dashboards.

Metricas importantes:

- Latencia HTTP por endpoint.
- Requests por status code.
- Erros 4xx e 5xx.
- Tempo de resposta dos endpoints principais.
- Uso de memoria, threads e GC da JVM.
- Conexoes com PostgreSQL.
- Uso do Redis.
- Cache hits e cache misses.
- Tentativas de login bem-sucedidas e falhas.
- Boards criados.
- Tarefas criadas.
- Tarefas movidas.
- Convites enviados.

Perguntas que os dashboards devem responder:

- A API esta lenta?
- Quais endpoints mais falham?
- O Redis esta ajudando de verdade?
- O endpoint de mover tarefa gera gargalo?
- Existem muitas tentativas de login falhas?
- O banco esta sofrendo com conexoes ou queries lentas?

## Modelo de Dados Inicial

Entidades principais:

```text
users
workspaces
workspace_members
workspace_invitations
boards
board_columns
tasks
task_activity
refresh_tokens
```

Relacionamentos principais:

```text
users 1:N workspace_members
workspaces 1:N workspace_members
workspaces 1:N boards
boards 1:N board_columns
board_columns 1:N tasks
tasks 1:N task_activity
```

Campos importantes:

- `workspace_id` em entidades sensiveis ao tenant.
- `created_at` e `updated_at`.
- `created_by`.
- `position` para ordenacao de colunas e tarefas.
- `deleted_at` ou `archived_at` quando fizer sentido.

## Cuidados Tecnicos Importantes

### Isolamento de Dados

O maior risco do projeto e vazamento entre tenants.

Toda operacao deve validar:

1. O usuario esta autenticado?
2. O usuario pertence ao workspace?
3. O usuario tem permissao para essa acao?
4. O recurso pertence ao workspace informado?

### Transacoes

Movimentacao de tarefas deve ser transacional.

Exemplo:

- Remover tarefa da posicao antiga.
- Inserir tarefa na nova coluna.
- Recalcular posicoes se necessario.
- Criar evento de activity log.
- Invalidar cache do board.

Tudo isso deve ser tratado como uma unidade consistente.

### Indices

Indices importantes:

```text
workspace_members(workspace_id, user_id)
boards(workspace_id)
board_columns(board_id)
tasks(workspace_id, board_id, column_id)
tasks(column_id, position)
task_activity(workspace_id, task_id)
workspace_invitations(workspace_id, email)
```

### Testes

Testes de maior valor:

- Usuario nao acessa workspace de outro usuario.
- Usuario viewer nao edita tarefas.
- Usuario member move tarefa corretamente.
- Cache e invalidado ao alterar board.
- Rate limit bloqueia excesso de tentativas.
- Movimento de tarefa preserva ordenacao.
- Refresh token funciona e pode ser revogado.

## Roadmap por Fases

## Fase 1: Fundacao do Projeto

Objetivo: criar a base tecnica para evoluir com seguranca.

Entregas:

- Estrutura do backend Spring Boot.
- Estrutura do frontend React + TypeScript.
- Docker Compose com PostgreSQL, Redis, backend e frontend.
- Configuracao de ambientes locais.
- Migrations do banco.
- Padrao basico de erros da API.
- Configuracao inicial de CORS.
- Health check da API.

Backend:

- Configurar Spring Boot.
- Configurar PostgreSQL.
- Configurar Flyway ou Liquibase.
- Criar estrutura de pacotes.
- Criar endpoint `/health` ou usar Actuator.

Frontend:

- Configurar rotas.
- Criar layout base.
- Criar tela inicial autenticada e nao autenticada.
- Configurar client HTTP.

Criterio de pronto:

- Projeto sobe localmente com Docker Compose.
- Backend conecta no banco.
- Frontend chama a API.
- Existe um fluxo minimo validavel no ambiente local.

## Fase 2: Autenticacao

Objetivo: implementar login, registro e sessao de forma segura.

Entregas:

- Registro de usuario.
- Login.
- Access token JWT.
- Refresh token.
- Logout.
- Hash de senha.
- Endpoint para obter o usuario atual.
- Protecao de rotas no frontend.

Backend:

- Criar entidade `User`.
- Criar endpoints de auth.
- Configurar Spring Security.
- Implementar geracao e validacao de JWT.
- Implementar refresh token.
- Criar testes de auth.

Frontend:

- Tela de login.
- Tela de cadastro.
- Armazenamento seguro do estado autenticado.
- Protecao de rotas privadas.

Criterio de pronto:

- Usuario consegue criar conta.
- Usuario consegue logar.
- Usuario consegue acessar uma rota protegida.
- Usuario consegue sair.
- Rotas protegidas rejeitam usuarios sem token valido.

## Fase 3: Workspaces e Multi-Tenancy

Objetivo: garantir isolamento entre workspaces e preparar a base SaaS.

Entregas:

- Criar workspace.
- Listar workspaces do usuario.
- Selecionar workspace ativo.
- Criar membership.
- Implementar roles iniciais.
- Validar `workspace_id` nas operacoes.

Backend:

- Criar entidades `Workspace` e `WorkspaceMember`.
- Criar servico de autorizacao por workspace.
- Criar endpoints de workspace.
- Criar testes de isolamento entre tenants.

Frontend:

- Tela/lista de workspaces.
- Seletor de workspace ativo.
- Fluxo de criacao de workspace.

Criterio de pronto:

- Usuario consegue criar workspace.
- Usuario consegue alternar entre workspaces.
- Usuario nao consegue acessar workspace do qual nao e membro.
- Testes cobrem tentativas de acesso cross-tenant.

## Fase 4: Kanban Funcional

Objetivo: entregar o produto principal funcionando.

Entregas:

- Criar boards.
- Criar colunas.
- Criar tarefas.
- Editar tarefas.
- Mover tarefas entre colunas.
- Reordenar tarefas.
- Interface de kanban com drag and drop.

Backend:

- Criar entidades `Board`, `BoardColumn` e `Task`.
- Criar endpoints para CRUD de boards, colunas e tarefas.
- Implementar operacao transacional para mover tarefa.
- Criar indices iniciais.
- Criar testes de movimentacao e ordenacao.

Frontend:

- Tela do board.
- Colunas com tarefas.
- Modal ou painel para criar/editar tarefa.
- Drag and drop.
- Estados de loading e erro.

Criterio de pronto:

- Usuario consegue criar um board completo.
- Usuario consegue mover tarefas entre colunas.
- Ordem das tarefas permanece correta apos recarregar.
- Backend valida permissoes antes de qualquer alteracao.

## Fase 5: Convites, Permissoes e Colaboracao

Objetivo: tornar o SaaS colaborativo e demonstrar controle de acesso real.

Entregas:

- Convite de membros.
- Aceite de convite.
- Alteracao de roles.
- Remocao de membros.
- Restricoes por papel.
- Viewer somente leitura.

Backend:

- Criar entidade `WorkspaceInvitation`.
- Criar endpoints de convite.
- Criar regras de autorizacao por role.
- Criar testes de permissao.

Frontend:

- Tela de membros.
- Acao de convidar membro.
- Indicacao visual de role.
- UI respeitando permissoes do usuario.

Criterio de pronto:

- Owner/Admin conseguem convidar membros.
- Member nao consegue gerenciar membros.
- Viewer nao consegue alterar boards ou tarefas.
- Backend bloqueia acoes indevidas mesmo que o frontend tente chama-las.

## Fase 6: Redis, Cache e Performance

Objetivo: adicionar Redis com uso pratico e mensuravel.

Entregas:

- Cache da visualizacao de board.
- Invalidacao de cache em alteracoes.
- Rate limit de login/reset de senha.
- Cache de permissoes, se fizer sentido.
- Testes ou logs demonstrando hit/miss.

Backend:

- Configurar Redis.
- Implementar estrategia de cache.
- Definir chaves padronizadas.
- Invalidar cache em writes relevantes.
- Adicionar rate limiting.

Criterio de pronto:

- Board acessado repetidamente usa cache.
- Alterar tarefa invalida cache corretamente.
- Login sofre rate limit apos excesso de tentativas.
- README ou documentacao explica onde Redis e usado.

## Fase 7: Observabilidade com Prometheus e Grafana

Objetivo: mostrar maturidade operacional e capacidade de diagnostico.

Entregas:

- Spring Boot Actuator.
- Micrometer.
- Endpoint Prometheus.
- Prometheus no Docker Compose.
- Grafana no Docker Compose.
- Dashboard da API.
- Dashboard de Redis/Postgres, se possivel.
- Metricas customizadas de negocio.

Backend:

- Expor metricas em `/actuator/prometheus`.
- Criar counters para eventos de negocio.
- Medir criacao e movimentacao de tarefas.
- Medir tentativas de login.
- Configurar tags uteis para endpoints e status.

Infra:

- Adicionar Prometheus ao Docker Compose.
- Adicionar Grafana ao Docker Compose.
- Criar configuracao `prometheus.yml`.
- Versionar dashboards ou documentar importacao.

Criterio de pronto:

- Prometheus coleta metricas da API.
- Grafana exibe dashboards.
- Existe print ou documentacao dos dashboards.
- E possivel observar latencia, erros, volume de requests e eventos principais do produto.

## Fase 8: Qualidade, CI/CD e Deploy

Objetivo: preparar o projeto para ser apresentado como portfolio profissional.

Entregas:

- Testes automatizados no backend.
- Testes relevantes no frontend.
- GitHub Actions.
- Build do backend.
- Build do frontend.
- Ambiente de staging ou producao.
- Variaveis de ambiente documentadas.
- README completo.

Backend:

- Testes unitarios.
- Testes de integracao.
- Testcontainers para PostgreSQL e Redis, se possivel.
- Validacao de migrations.

Frontend:

- Testes de componentes principais.
- Testes de fluxo critico, se o escopo permitir.
- Build validado em CI.

Infra:

- Pipeline de CI.
- Deploy da API.
- Deploy do frontend.
- Banco e Redis gerenciados ou via container, dependendo do provedor.

Criterio de pronto:

- CI roda em pull requests.
- Aplicacao esta acessivel online.
- README explica como rodar localmente.
- README tem arquitetura, screenshots e decisoes tecnicas.

## Fase 9: Polish de Portfolio

Objetivo: deixar o projeto facil de avaliar por recrutadores, devs e visitantes.

Entregas:

- README raiz bem escrito.
- Prints da aplicacao.
- Prints do Grafana.
- Diagrama simples da arquitetura.
- Explicacao sobre multi-tenancy.
- Explicacao sobre Redis.
- Explicacao sobre observabilidade.
- Usuario ou workspace demo.
- Dados seed para avaliacao rapida.

Ideias para o README:

- "Por que este projeto existe"
- "Principais aprendizados"
- "Arquitetura"
- "Multi-tenancy"
- "Seguranca"
- "Cache com Redis"
- "Observabilidade"
- "Como rodar localmente"
- "Como testar"
- "Roadmap futuro"

Criterio de pronto:

- Uma pessoa externa consegue entender o valor tecnico do projeto em poucos minutos.
- Uma pessoa externa consegue rodar o projeto localmente.
- O deploy online demonstra o fluxo principal.
- O projeto comunica claramente as decisoes tecnicas tomadas.

## Fora do MVP

Esses itens podem ser adicionados depois, mas nao devem bloquear o core:

- Billing real com Stripe.
- Comentarios complexos em tarefas.
- Anexos de arquivos.
- Chat em tempo real.
- WebSocket completo.
- Notificacoes sofisticadas.
- Relatorios avancados.
- Templates de board.
- Customizacao visual profunda.
- Aplicativo mobile.

## Possiveis Melhorias Futuras

- WebSocket ou Server-Sent Events para atualizacao em tempo real.
- Notificacoes por email.
- Comentarios em tarefas.
- Labels e prioridades.
- Filtros por responsavel, status e data.
- Busca textual.
- Templates de boards.
- Exportacao de dados.
- Billing fake ou sandbox com Stripe para demonstrar SaaS completo.
- Feature flags.
- Outbox pattern para eventos.
- Arquitetura orientada a eventos em uma fase mais avancada.

## Ordem Recomendada de Execucao

```text
1. Fundacao do projeto
2. Autenticacao
3. Workspaces e multi-tenancy
4. Kanban funcional
5. Convites, permissoes e colaboracao
6. Redis, cache e performance
7. Prometheus, Grafana e observabilidade
8. CI/CD e deploy
9. Polish de portfolio
```

## Principio Guia

O projeto deve ser pequeno o suficiente para ser terminado, mas serio o suficiente para demonstrar maturidade tecnica.

O diferencial nao esta em ter muitas features. O diferencial esta em mostrar:

- Isolamento multi-tenant bem feito.
- Autenticacao segura.
- Autorizacao consistente.
- Transacoes corretas.
- Cache com invalidacao clara.
- Observabilidade real.
- Testes cobrindo riscos importantes.
- Deploy funcional.
- Documentacao clara.

