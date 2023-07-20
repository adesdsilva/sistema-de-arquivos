package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Scanner;

public enum Command {

    LIST() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
        }

        @Override
        Path execute(Path path) throws IOException {

            File diretorio = new File(path.toUri());

            // Verifica se o caminho especificado é um diretório válido
            if (diretorio.isDirectory()) {
                // Lista os arquivos e diretórios presentes no diretório
                File[] conteudo = diretorio.listFiles();

                if (conteudo != null) {
                    System.out.println("Conteúdo do diretório: " + path);
                    for (File arquivoOuDiretorio : conteudo) {
                        if (arquivoOuDiretorio.isDirectory()) {
                            System.out.println("[D] " + arquivoOuDiretorio.getName());
                        } else {
                            System.out.println("[F] " + arquivoOuDiretorio.getName());
                        }
                    }
                } else {
                    System.out.println("O diretório está vazio.");
                }
            } else {
                System.out.println("O caminho especificado não é um diretório válido.");
            }

            return path;
        }
    },
    SHOW() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("SHOW") || commands[0].startsWith("show");
        }

        @Override
        Path execute(Path path) {

            String caminho = path.toString() + "/" + parameters[1];
            File arquivo = new File(caminho);

            // Verifica se o caminho especificado é um arquivo válido
            if (arquivo.isFile()) {
                try {
                    Scanner scanner = new Scanner(arquivo);

                    System.out.println("Conteúdo do arquivo " + path + ":");
                    while (scanner.hasNextLine()) {
                        String linha = scanner.nextLine();
                        System.out.println(linha);
                    }

                    scanner.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Erro ao ler o arquivo: " + e.getMessage());
                }
            } else {
                System.out.println("Erro: O caminho especificado não é um arquivo válido. (Extensão não suportada!)");
            }
            //path = Path.of(path.toString() + "/" + parameters[1]);
            return path;
        }
    },
    BACK() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("BACK") || commands[0].startsWith("back");
        }

        @Override
        Path execute(Path path) {



            return path;
        }
    },
    OPEN() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("OPEN") || commands[0].startsWith("open");
        }

        @Override
        Path execute(Path path) {

            String caminhoDiretorio = path.toString() + "/" + parameters[1];
            File diretorio = new File(caminhoDiretorio);

            if (diretorio.exists()) {
                if (diretorio.isDirectory()) {
                    System.out.println("O diretório existe e pode ser acessado: " + caminhoDiretorio);
                } else {
                    System.out.println("O caminho informado não corresponde a um diretório válido.");
                }
            } else {
                System.out.println("O diretório não existe ou o caminho informado está incorreto.");
            }
            path = Path.of(path.toString() + "/" + parameters[1]);
            return path;
        }
    },
    DETAIL() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("DETAIL") || commands[0].startsWith("detail");
        }

        @Override
        Path execute(Path path) {
            path = Path.of(path.toString() + "/" + parameters[1]);
            try {
                BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
                System.out.println("Data de criação: " + new Date(attributes.creationTime().toMillis()));
                System.out.println("Última modificação: " + new Date(attributes.lastModifiedTime().toMillis()));
            } catch (IOException e) {
                System.out.println("Ocorreu um erro ao obter as informações do arquivo ou diretório.");
                e.printStackTrace();
            }
            path = Path.of(path.toString() + "/" + parameters[1]);
            return path;
        }
    },
    EXIT() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("EXIT") || commands[0].startsWith("exit");
        }

        @Override
        Path execute(Path path) {
            System.out.print("Saindo...");
            return path;
        }

        @Override
        boolean shouldStop() {
            return true;
        }
    };

    abstract Path execute(Path path) throws IOException;

    abstract boolean accept(String command);

    void setParameters(String[] parameters) {
    }

    boolean shouldStop() {
        return false;
    }

    public static Command parseCommand(String commandToParse) {

        if (commandToParse.isBlank()) {
            throw new UnsupportedOperationException("Type something...");
        }

        final var possibleCommands = values();

        for (Command possibleCommand : possibleCommands) {
            if (possibleCommand.accept(commandToParse)) {
                possibleCommand.setParameters(commandToParse.split(" "));
                return possibleCommand;
            }
        }

        throw new UnsupportedOperationException("Can't parse command [%s]".formatted(commandToParse));
    }
}
