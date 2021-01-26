import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        String bound = "____________________________________________________________";
        String topBound = "____________________________________________________________\n";
        String bottomBound = "\n____________________________________________________________\n";
        Scanner sc = new Scanner(System.in);
        DukeList list = new DukeList();
        String path = "data/duke.txt";
        File dir = new File("data");
        File f = new File(path);
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            } else { //read the file and make the  duke obj
                readFromFile(path, list);
            }
            if (!f.exists()) f.createNewFile();
        } catch (IOException e) {
            System.out.println("Something went wrong!");
            sc.close();
        }

        System.out.println(topBound + logo + "\nHello! I'm Duke\nWhat can I do for you today?" + bottomBound);

        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] arr = input.split("/");
            if (input.equals("bye")) {
                try {
                    list.writeToFile(path);
                    System.out.println(topBound + "Good bye! Stay calm and keep coding o7" + bottomBound);
                    sc.close();
                } catch (IOException e) {
                    System.out.println("Error in saving list!");
                }
                break;
            } else if (input.equals("list")) {
                System.out.println(bound);
                System.out.println("Here are the tasks in your list");
                list.printAll();
                System.out.println(topBound);
            } else if (arr[0].startsWith("done")) {
                try {
                    String[] doneArr = arr[0].split(" ");
                    int x = Integer.parseInt(doneArr[1]) - 1;
                    list.done(x);
                    System.out.println(topBound + "Good job! I've marked this task as done:\n" + "  " + list.get(x)
                            + bottomBound);
                } catch (IndexOutOfBoundsException e) {
                    // Task number x does not exist
                    System.out.println(topBound + "Task does not exist and can not be completed!" + bottomBound);
                } catch (NumberFormatException e) {
                    // parameter for done is not an Integer
                    System.out.println(topBound + "done must be followed by an Integer ie. done 1. Try again!"
                            + bottomBound);
                }
            } else if (arr[0].startsWith("todo")) {
                try {
                    String taskName = input.replaceFirst("todo", "").stripLeading();
                    if (taskName.equals("")) {
                        throw new DukeException("todo");
                    }
                    ToDos curr = new ToDos(taskName);
                    list.add(curr);
                    System.out.println(topBound + "Got it! I've added this task:\n" + "  " + curr
                            + "\nNow you have " + list.noOfTasks() + " tasks in the list." + bottomBound);
                } catch (DukeException e) {
                    // command came without a description
                    System.out.println(topBound + e.getMessage() + bottomBound);
                }
            } else if (arr[0].startsWith("deadline")) {
                try {
                    String taskName = arr[0].replaceFirst("deadline", "").stripLeading()
                            .stripTrailing();
                    if (taskName.equals("")) {
                        throw new DukeException("deadline");
                    }
                    Deadlines curr = new Deadlines(taskName, arr[1].replaceFirst("by", "")
                            .stripLeading());
                    list.add(curr);
                    System.out.println(topBound + "Got it! I've added this task:\n" + "  " + curr
                            + "\nNow you have " + list.noOfTasks() + " tasks in the list." + bottomBound);
                } catch (DukeException e) {
                    // command came without a description
                    System.out.println(topBound + e.getMessage() + bottomBound);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(topBound + "deadline must include '/by time'. Try again!" + bottomBound);
                }
            } else if (arr[0].startsWith("event")) {
                try {
                    String taskName = arr[0].replaceFirst("event", "").stripLeading().stripTrailing();
                    if (taskName.equals("")) {
                        throw new DukeException("event");
                    }
                    Events curr = new Events(taskName, arr[1].replaceFirst("at", "").stripLeading());
                    list.add(curr);
                    System.out.println(topBound + "Got it! I've added this task:\n" + "  " + curr
                            + "\nNow you have " + list.noOfTasks() + " tasks in the list." + bottomBound);
                } catch (DukeException e) {
                    // command came without a description
                    System.out.println(topBound + e.getMessage() + bottomBound);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(topBound + "event must include '/at duration'. Try again!" + bottomBound);
                }
            } else if (arr[0].startsWith("delete")) {
                try {
                    String str = arr[0].replaceFirst("delete", "").stripLeading();
                    int x = Integer.parseInt(str) - 1;
                    Task curr = list.get(x);
                    list.delete(x);
                    System.out.println(topBound + "Got it! I've removed this task:\n" + " " + curr + "\n" + "You have "
                            + list.noOfTasks() + " tasks left in the list." + bottomBound);
                } catch (IndexOutOfBoundsException e) {
                    // task number x does not exist
                    System.out.println(topBound + "Task does not exist and cannot be deleted!" + bottomBound);
                } catch (NumberFormatException e) {
                    // parameter for delete is not an Integer
                    System.out.println(topBound + "delete must be followed by an Integer, ie. delete 1. Try again!"
                            + bottomBound);
                }
            } else {
                // unknown command
                System.out.println(topBound + "OOPS!!! I'm sorry, but I don't know what that means :-(" + bottomBound);
            }
        }
    }
    public static void readFromFile(String filePath, DukeList list) throws FileNotFoundException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            String curr = s.nextLine();
            String[] currArray = curr.split("\\|");

            if (currArray[0].equals("T")) {
                ToDos currTask;
                if (currArray[1].equals("0")) {
                    currTask = new ToDos(currArray[2]);
                } else {
                    currTask = new ToDos(currArray[2], true);
                }
                list.add(currTask);
            } else if (currArray[0].equals("D")) {
                Deadlines currTask;
                if (currArray[1].equals("0")) {
                    currTask = new Deadlines(currArray[2], currArray[3]);
                } else {
                    currTask = new Deadlines(currArray[2], currArray[3], true);
                }
                list.add(currTask);
            } else if (currArray[0].equals("E")) {
                Events currTask;
                if (currArray[1].equals("0")) {
                    currTask = new Events(currArray[2], currArray[3]);
                } else {
                    currTask = new Events(currArray[2], currArray[3], true);
                }
                list.add(currTask);
            }
        }
    }
}
