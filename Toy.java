import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Toy implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private int quantity;
    private double weightPercentage;

    public Toy(int id, String name, int quantity, double weightPercentage) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weightPercentage = weightPercentage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getWeightPercentage() {
        return weightPercentage;
    }

    public void setWeightPercentage(double weightPercentage) {
        this.weightPercentage = weightPercentage;
    }

    @Override
    public String toString() {
        return "Toy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", weightPercentage=" + weightPercentage +
                '}';
    }

    public static void main(String[] args) {
        ToyStore toyStore = new ToyStore();

        toyStore.addNewToy(new Toy(1, "Teddy Bear", 5, 30.0));
        toyStore.addNewToy(new Toy(2, "Building Blocks", 10, 20.0));
        toyStore.addNewToy(new Toy(3, "Doll", 8, 15.0));
        toyStore.addNewToy(new Toy(4, "Toy Car", 7, 10.0));
        toyStore.addNewToy(new Toy(5, "Puzzle", 4, 25.0));

        toyStore.updateToyWeight(1, 35.0);

        Toy randomToy = toyStore.drawRandomToy();
        if (randomToy != null) {
            toyStore.savePrizeToy(randomToy);
            System.out.println("Randomly selected toy: " + randomToy);
        } else {
            System.out.println("No toys available for drawing.");
        }
    }
}

class ToyStore {
    private Queue<Toy> toys;

    public ToyStore() {
        this.toys = new LinkedList<>();
    }

    public void addNewToy(Toy toy) {
        toys.add(toy);
    }

    public void updateToyWeight(int toyId, double weightPercentage) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeightPercentage(weightPercentage);
                break;
            }
        }
    }

    public Toy drawRandomToy() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeightPercentage).sum();
        double randomNumber = new Random().nextDouble() * totalWeight;
        double currentWeight = 0;

        for (Toy toy : toys) {
            currentWeight += toy.getWeightPercentage();
            if (randomNumber < currentWeight) {
                toys.remove(toy);
                toy.setQuantity(toy.getQuantity() - 1); 
                return toy;
            }
        }

        return null;
    }

    public void savePrizeToy(Toy toy) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("prize_toy.txt"))) {
            oos.writeObject(toy);
            System.out.println("Prize toy saved successfully.");
        } catch (IOException e) {
            System.err.println("Error while saving prize toy: " + e.getMessage());
        }
    }
}