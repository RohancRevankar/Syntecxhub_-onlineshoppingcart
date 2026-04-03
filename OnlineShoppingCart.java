import java.util.*;

public class OnlineShoppingCart {

    // Product catalog: name -> price
    static HashMap<String, Double> catalog = new HashMap<>();

    // Cart: name -> quantity
    static HashMap<String, Integer> cart = new HashMap<>();

    // Ordered list of cart items (for display order)
    static ArrayList<String> cartOrder = new ArrayList<>();

    public static void main(String[] args) {
        initCatalog();
        Scanner sc = new Scanner(System.in);
        int choice;

        System.out.println("====================================");
        System.out.println("   Welcome to the Online Shop!    ");
        System.out.println("====================================");

        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1. View Product Catalog");
            System.out.println("2. Add Item to Cart");
            System.out.println("3. Remove Item from Cart");
            System.out.println("4. Update Item Quantity");
            System.out.println("5. View Cart");
            System.out.println("6. Calculate Total");
            System.out.println("7. Checkout");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> viewCatalog();
                case 2 -> addItem(sc);
                case 3 -> removeItem(sc);
                case 4 -> updateQuantity(sc);
                case 5 -> viewCart();
                case 6 -> calculateTotal();
                case 7 -> checkout(sc);
                case 8 -> System.out.println("\nThank you for shopping! Goodbye!");
                default -> System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 8);

        sc.close();
    }

    // Initialize product catalog
    static void initCatalog() {
        catalog.put("Apple",       30.00);
        catalog.put("Banana",      15.00);
        catalog.put("Milk",        55.00);
        catalog.put("Bread",       40.00);
        catalog.put("Eggs",        80.00);
        catalog.put("Rice",       120.00);
        catalog.put("Butter",      90.00);
        catalog.put("Cheese",     150.00);
        catalog.put("Juice",       60.00);
        catalog.put("Chocolate",   45.00);
    }

    // Display catalog
    static void viewCatalog() {
        System.out.println("\n===== PRODUCT CATALOG =====");
        System.out.printf("%-15s %s%n", "Product", "Price (₹)");
        System.out.println("---------------------------");
        for (Map.Entry<String, Double> entry : catalog.entrySet()) {
            System.out.printf("%-15s ₹%.2f%n", entry.getKey(), entry.getValue());
        }
    }

    // Add item to cart
    static void addItem(Scanner sc) {
        viewCatalog();
        System.out.print("\nEnter product name to add: ");
        String name = capitalize(sc.nextLine().trim());

        if (!catalog.containsKey(name)) {
            System.out.println("Product not found in catalog!");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();
        sc.nextLine();

        if (qty <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        if (cart.containsKey(name)) {
            cart.put(name, cart.get(name) + qty);
            System.out.println(qty + " more " + name + "(s) added. Total: " + cart.get(name));
        } else {
            cart.put(name, qty);
            cartOrder.add(name);
            System.out.println(name + " x" + qty + " added to cart!");
        }
    }

    // Remove item from cart
    static void removeItem(Scanner sc) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }
        viewCart();
        System.out.print("\nEnter product name to remove: ");
        String name = capitalize(sc.nextLine().trim());

        if (cart.containsKey(name)) {
            cart.remove(name);
            cartOrder.remove(name);
            System.out.println(name + " removed from cart.");
        } else {
            System.out.println("Item not found in cart.");
        }
    }

    // Update quantity of an item
    static void updateQuantity(Scanner sc) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }
        viewCart();
        System.out.print("\nEnter product name to update: ");
        String name = capitalize(sc.nextLine().trim());

        if (!cart.containsKey(name)) {
            System.out.println("Item not found in cart.");
            return;
        }

        System.out.print("Enter new quantity (0 to remove): ");
        int qty = sc.nextInt();
        sc.nextLine();

        if (qty <= 0) {
            cart.remove(name);
            cartOrder.remove(name);
            System.out.println(name + " removed from cart.");
        } else {
            cart.put(name, qty);
            System.out.println(name + " quantity updated to " + qty + ".");
        }
    }

    // Display cart contents
    static void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is empty!");
            return;
        }
        System.out.println("\n========== YOUR CART ==========");
        System.out.printf("%-15s %-8s %-10s %s%n", "Product", "Qty", "Price", "Subtotal");
        System.out.println("------------------------------------------------");
        for (String name : cartOrder) {
            int qty = cart.get(name);
            double price = catalog.get(name);
            double subtotal = qty * price;
            System.out.printf("%-15s %-8d ₹%-9.2f ₹%.2f%n", name, qty, price, subtotal);
        }
        System.out.println("------------------------------------------------");
    }

    // Calculate and display total
    static void calculateTotal() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }
        double total = getTotal();
        viewCart();
        System.out.printf("%-15s %29s%n", "TOTAL", "₹" + String.format("%.2f", total));
    }

    // Compute total price
    static double getTotal() {
        double total = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            total += catalog.get(entry.getKey()) * entry.getValue();
        }
        return total;
    }

    // Checkout
    static void checkout(Scanner sc) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Nothing to checkout!");
            return;
        }
        double total = getTotal();
        calculateTotal();
        System.out.printf("%nTotal Amount Payable: ₹%.2f%n", total);
        System.out.print("Confirm checkout? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            System.out.println("\n✅ Order placed successfully!");
            System.out.println("Your items will be delivered soon. Thank you!");
            cart.clear();
            cartOrder.clear();
        } else {
            System.out.println("Checkout cancelled. Items still in cart.");
        }
    }

    // Capitalize first letter for consistent key lookup
    static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}