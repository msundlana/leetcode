package main;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PassingYearBooks {

    class Student {
        int number;
        int numberOfPasses;
        int passedTo;


        public Student(int number, int numberOfPasses,  int passedTo ) {
            this.number = number;
            this.numberOfPasses = numberOfPasses;
            this.passedTo = passedTo;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumberOfPasses() {
            return numberOfPasses;
        }

        public void setNumberOfPasses(int numberOfPasses) {
            this.numberOfPasses = numberOfPasses;
        }


        public int getPassedTo() {
            return passedTo;
        }

        public void setPassedTo(int passedTo) {
            this.passedTo = passedTo;
        }
    }



    private Map<Integer,Student> intialiazeStudentMap(int[] arr){
        Map<Integer,Student> studentPassBookMap = new ConcurrentHashMap<>();
        for(int i=1; i<=arr.length; i++){
            studentPassBookMap.put(i, new Student(i, 1, arr[i - 1]));
        }
        return studentPassBookMap;
    }

    int[] findSignatureCounts(int[] arr) {
        // Write your code here
        int [] output = new int[arr.length];
        Integer countNumberOfStudentsRecievedBookBack = new Integer(0);
        Map<Integer,Student> studentPassBookMap =  intialiazeStudentMap(arr);
        while (countNumberOfStudentsRecievedBookBack!=arr.length){

            for (Student studentPassBook: studentPassBookMap.values()) {
                System.out.println("In " + studentPassBook.getNumber());
                System.out.println("Passed to " + studentPassBook.getPassedTo());
                if(studentPassBook.getPassedTo()==studentPassBook.getNumber()){
                    countNumberOfStudentsRecievedBookBack++;
                    output[studentPassBook.getNumber()-1] = studentPassBook.getNumberOfPasses();
                    studentPassBookMap.remove(studentPassBook.getNumber());

                }else{
                    int studentPassBookPassedTo = arr[studentPassBook.getPassedTo() - 1];
                    studentPassBook.setPassedTo(studentPassBookPassedTo);
                    studentPassBook.setNumberOfPasses(studentPassBook.getNumberOfPasses() + 1);

                }

            }
        }
        return output;
    }

    void printIntegerArray(int[] arr) {
        int len = arr.length;
        System.out.print("[");
        for(int i = 0; i < len; i++) {
            if (i != 0) {
                System.out.print(", ");
            }
            System.out.print(arr[i]);
        }
        System.out.print("]");
    }


    public static void main(String [] args){
        PassingYearBooks passingYearBooks = new PassingYearBooks();
        int[] arr = {1, 2};
        passingYearBooks.printIntegerArray(passingYearBooks.findSignatureCounts(arr));
    }
}
