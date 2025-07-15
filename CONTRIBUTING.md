# Guia de Contribuição do Gerenciador de Pedidos

Primeiramente, obrigado pelo seu interesse em contribuir para este projeto! 🎉

Seja uma correção de bug, uma nova funcionalidade ou uma melhoria na documentação, toda contribuição é bem-vinda e valorizada.

## Código de Conduta

Este projeto e todos que participam dele são regidos pelo nosso [Código de Conduta](CODE_OF_CONDUCT.md). Ao participar, você concorda em seguir seus termos.

## Como Contribuir

### Reportando Bugs
- Utilize a seção de **Issues** do GitHub para reportar bugs.
- Verifique se o bug já não foi reportado antes.
- Descreva o bug com o máximo de detalhes possível: o que você esperava que acontecesse e o que realmente aconteceu. Inclua logs de erro, se aplicável.

### Sugerindo Melhorias
- Utilize a seção de **Issues** para sugerir melhorias, marcando com a label `enhancement`.
- Explique de forma clara e concisa a sua sugestão e por que ela seria benéfica para o projeto.

### Fluxo de Desenvolvimento (Pull Requests)

1.  **Faça o Fork** do repositório.
2.  **Crie uma nova Branch** a partir da `main`: `git checkout -b feature/nome-da-sua-feature`.
3.  **Faça suas alterações.** Garanta que o código segue os padrões de estilo do projeto.
4.  **Adicione testes** para cobrir suas novas alterações. Testes são inegociáveis.
5.  **Garanta que todos os testes estão passando** executando `mvn clean install`.
6.  **Faça o Commit** de suas mudanças seguindo nosso Padrão de Commits.
7.  **Abra um Pull Request** contra a branch `main` do repositório original.

## Padrão de Commits (Conventional Commits)

Para manter um histórico de commits limpo e significativo, utilizamos a especificação [**Conventional Commits**](https://www.conventionalcommits.org/en/v1.0.0/). Isso nos permite gerar changelogs de forma automatizada e entender facilmente o propósito de cada mudança.

O formato é: `<tipo>(<escopo>): <descrição>`

- **Tipos comuns:**
    - `feat`: Uma nova funcionalidade.
    - `fix`: Uma correção de bug.
    - `docs`: Mudanças na documentação.
    - `style`: Formatação, ponto e vírgula, etc. (sem mudança na lógica).
    - `refactor`: Refatoração de código que não corrige um bug nem adiciona uma feature.
    - `test`: Adição ou correção de testes.
    - `chore`: Atualização de tarefas de build, dependências, etc.

**Exemplos:**
- `feat(auth): adicionar endpoint de login com JWT`
- `fix(produto): corrigir cálculo de preço com desconto`
- `docs(readme): atualizar tabela de endpoints da API`

Obrigado novamente pela sua contribuição!