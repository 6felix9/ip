package lyra;

import java.time.LocalDateTime;

/**
 * Holds parsed data from an update command.
 */
public class UpdateCommandData {
    private final int index;
    private final String updateType;
    private final String descriptionValue;
    private final LocalDateTime dateValue;

    /**
     * Creates UpdateCommandData for a description update.
     */
    public UpdateCommandData(int index, String descriptionValue) {
        this.index = index;
        this.updateType = UpdateType.DESCRIPTION;
        this.descriptionValue = descriptionValue;
        this.dateValue = null;
    }

    /**
     * Creates UpdateCommandData for a date update (by, from, or to).
     */
    public UpdateCommandData(int index, String updateType, LocalDateTime dateValue) {
        this.index = index;
        this.updateType = updateType;
        this.descriptionValue = null;
        this.dateValue = dateValue;
    }

    public int getIndex() {
        return index;
    }

    public String getUpdateType() {
        return updateType;
    }

    public String getDescriptionValue() {
        return descriptionValue;
    }

    public LocalDateTime getDateValue() {
        return dateValue;
    }
}
