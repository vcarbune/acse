import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class LogFormatter extends SimpleFormatter {

    @Override
    public String format (LogRecord record) {
        return record.getMessage();
    }
}
