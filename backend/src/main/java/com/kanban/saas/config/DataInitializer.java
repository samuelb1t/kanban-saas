package com.kanban.saas.config;

import com.kanban.saas.model.entities.*;
import com.kanban.saas.model.enums.Role;
import com.kanban.saas.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDatabase() {
        return args -> {
            // Evita reinserção se o banco já tiver dados
            if (userRepository.count() > 0) {
                log.info("Banco já possui dados. Seed ignorado.");
                return;
            }

            log.info("Iniciando seed do banco de dados...");

            // --- Usuários ---
            User admin = userRepository.save(new User(
                    "Admin Demo",
                    "admin@gmail.com",
                    passwordEncoder.encode("1234")
            ));

            User alice = userRepository.save(new User(
                    "Samuel",
                    "samuel@gmail.com",
                    passwordEncoder.encode("1234")
            ));

            // --- Workspace ---
            Workspace workspace = new Workspace();
            workspace.setName("Projeto Demo");
            workspaceRepository.save(workspace);

            // --- Membros do workspace ---
            userWorkspaceRepository.save(new UserWorkspace(admin, workspace, Role.OWNER));
            userWorkspaceRepository.save(new UserWorkspace(alice, workspace, Role.EDITOR));

            // --- Board ---
            Board board = new Board();
            board.setName("Sprint 1");
            board.setWorkspace(workspace);
            boardRepository.save(board);

            // --- Colunas ---
            BoardColumn colBacklog = createColumn("Backlog", 1, board);
            BoardColumn colInProgress = createColumn("Em Progresso", 2, board);
            BoardColumn colDone = createColumn("Concluído", 3, board);

            // --- Tasks ---
            createTask("Configurar autenticação JWT", "Implementar login e registro com JWT", 1, colBacklog);
            createTask("Modelar entidades JPA", "Criar User, Workspace, Board, Column e Task", 2, colBacklog);
            createTask("Criar endpoints de Kanban", "CRUD de boards e colunas", 1, colInProgress);
            createTask("Deploy no Railway", "Configurar variáveis de ambiente e fazer o deploy", 1, colDone);

            log.info("Seed concluído com sucesso! {} usuários, {} workspaces, {} boards criados.",
                    userRepository.count(), workspaceRepository.count(), boardRepository.count());
        };
    }

    private BoardColumn createColumn(String name, int order, Board board) {
        BoardColumn col = new BoardColumn();
        col.setName(name);
        col.setOrder(order);
        col.setBoard(board);
        return boardColumnRepository.save(col);
    }

    private Task createTask(String name, String description, int order, BoardColumn column) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setOrder(order);
        task.setColumn(column);
        return taskRepository.save(task);
    }
}
