/*
 * Decompiled with CFR 0_110.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package ac.iiti.gkalyan0510.fastedge;

public class Note {
    private String description;
    private int position;
    private String title;

    public Note() {
    }

    public Note(int n, String string2, String string3) {
        this.position = n;
        this.title = string2;
        this.description = string3;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) return false;
        if (this.getClass() != object.getClass()) {
            return false;
        }
        Note note = (Note)object;
        if (this.position == note.position) return true;
        return false;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPosition() {
        return this.position;
    }

    public String getTitle() {
        return this.title;
    }

    public int hashCode() {
        return this.position;
    }

    public void setDescription(String string2) {
        this.description = string2;
    }

    public void setPosition(int n) {
        this.position = n;
    }

    public void setTitle(String string2) {
        this.title = string2;
    }
}

