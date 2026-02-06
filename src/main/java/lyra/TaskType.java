package lyra;

/**
 * Represents the types of tasks in Lyra.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    /**
     * Constructor for TaskType.
     */
    TaskType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Get the symbol.
     */
    public String getSymbol() {
        return symbol;
    }
}
