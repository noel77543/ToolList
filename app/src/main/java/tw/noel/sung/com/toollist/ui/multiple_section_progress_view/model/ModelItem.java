package tw.noel.sung.com.toollist.ui.multiple_section_progress_view.model;

public class ModelItem {

    private float[] sections;
    private int[] sectionColors;
    private float maxValue;
    private float targetValue;

    public ModelItem(float[] sections, int[] sectionColors, float maxValue, float targetValue) {
        this.sections = sections;
        this.sectionColors = sectionColors;
        this.maxValue = maxValue;
        this.targetValue = targetValue;
    }

    public int[] getSectionColors() {
        return sectionColors;
    }

    public float[] getSections() {
        return sections;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public float getTargetValue() {
        return targetValue;
    }
}
