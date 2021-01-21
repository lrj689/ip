import java.util.ArrayList;

public class DukeList {
    private ArrayList<Task> list;

    public DukeList() {
        this.list = new ArrayList<Task>();
    }

    public void add(Task item) {
        list.add(item);
    }

    public void done(int x) {
        list.get(x).markAsDone();
    }

    public int noOfTasks() {
        return list.size();
    }

    /**
     *
     * @param x Task number to be removed
     * deletes the stated task
     */

    public void delete(int x) {
        list.remove(x);
    }

    /**
     * DukeList getter
     * @param x Task number x
     * @return Task x
     */
    public Task get(int x) {
        return list.get(x);
    }

    /**
     * Prints DukeList object
     */
    public void printAll() {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            System.out.println(Integer.toString(i + 1) + "." + list.get(i));
        }
    }
}
