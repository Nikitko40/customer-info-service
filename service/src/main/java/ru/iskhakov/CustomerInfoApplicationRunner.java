package ru.iskhakov;

import ru.iskhakov.exception.DomainException;
import ru.iskhakov.serialize.ObjectSerializer;
import ru.iskhakov.serialize.out.ResultType;
import ru.iskhakov.serialize.out.SearchResult;
import ru.iskhakov.service.CustomerInfoService;
import ru.iskhakov.util.FileInputReaderUtil;
import ru.iskhakov.util.FileOutputWriterUtil;

import javax.annotation.Nonnull;

public class CustomerInfoApplicationRunner {

    private final static String SEARCH_COMMAND = "search";
    private final static String STAT_COMMAND = "stat";

    private final static String NOT_CORRECT_ARGS_ERROR_MESSAGE =
            "\nПожалуйста укажите аргументы по примеру:\n" +
                    "search(название операции) B:\\input.json(полный путь к файлу)";

    public static void main(String[] args) throws DomainException {
        String input;
        String output;
        String path = null;
        String command = args[0];

        try {
            path = validateArgumentsAndGetPath(args);
            input = FileInputReaderUtil.read(path);
            final CustomerInfoService service = new CustomerInfoService();
            if (command.equals(SEARCH_COMMAND)) {
                output = service.search(input);
            } else if (command.equals(STAT_COMMAND)) {
                output = service.stat(input);
            } else {
                throw new DomainException("Данная операция не поддерживается");
            }
            FileOutputWriterUtil.write(output, path);
            System.out.println("SUCCESS");

        } catch (Throwable e) {
            if (path == null) {
                throw new DomainException(NOT_CORRECT_ARGS_ERROR_MESSAGE);
            }
            createErrorOutput(path, e.getMessage());
        }
    }

    private static String validateArgumentsAndGetPath(String[] args) throws DomainException {
        int length = args.length;
        if (length > 2) {
            throw new DomainException("Указанно более 2 ух аргументов в запросе." +
                    NOT_CORRECT_ARGS_ERROR_MESSAGE);
        } else if (length == 1) {
            throw new DomainException("Не указан путь к файлу" +
                    NOT_CORRECT_ARGS_ERROR_MESSAGE);
        } else if (length == 0) {
            throw new DomainException(NOT_CORRECT_ARGS_ERROR_MESSAGE);
        }
        return args[1];
    }

    private static void createErrorOutput(@Nonnull String path, @Nonnull String errorMessage) throws DomainException {
        ObjectSerializer serializer = new ObjectSerializer();
        SearchResult jsonOutput = SearchResult.builder()
                .type(ResultType.ERROR)
                .message(errorMessage)
                .build();
        FileOutputWriterUtil.write(serializer.serializeSearch(jsonOutput), path);
        System.out.println("ERROR");

    }

}
