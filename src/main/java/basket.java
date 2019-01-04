import java.util.Arrays;
import java.util.List;
import java.io.*;

public class basket {
    private List<purchase> purchases;
    private int taxTotal;
    private int grandTotal;

    private void basket(String fileName){
        //In a more real-world situation, items might be identified by a PublicId or slug.
        //This exercise gave me input in plain text, so I'm going with that and parsing each input as a text file.
        try {
            FileReader input = new FileReader(fileName);
            BufferedReader bufRead = new BufferedReader(input);
            String myLine = null;

            while ( (myLine = bufRead.readLine()) != null)
            {
                // Assumptions must be made about input format.  I'm assuming here that all lines end with " at " followed by the price, as that's true in all provided examples.
                // I'm also assuming no item names will include " at ".
                String[] array1 = myLine.split(" at ", 2);
                // Another assumption I must make one way or the other as all examples provided are of exactly one unit:
                // From context, I would guess that the phrase " at <price>" denotes a unit price rather than the aggregate price, but that's only a guess.
                // It would be a high priority to verify that this was correct or to get any example with a multiple-unit purchase and total to verify.
                // Otherwise, there would be a division by number of units needed.
                int unitPrice = (int) Math.round (100. * Double.parseDouble(array1[1]));
                // Here I'm relying on each line beginning with an integer number of units followed by a space, then the item description in text:
                String[] array2 = array1[0].split(" ", 2);
                int amount = Integer.parseInt(array2[0]);
                String description = array2[1];
                // Now to a very clumsy part.  In practice it would be vastly better to have a database where item PublicIds or slugs would correlate to an entry with booleans for the taxes;
                // in theory, a sophisticated algorithm could try to figure out from strings and dictionaries whether arbitrary names counted as books, medical, or food products.
                // I'm going to do a quick and dirty method here, in practice this should probably be a database call, if I had made a database.
                String[] basicExemptNames = {"book", "chocolate bar", "imported box of chocolates", "packet of headache pills", "box of imported chocolates"};
                Boolean basicExempt = Arrays.asList(basicExemptNames).contains(description);
                // Also, I'm going to assume that all imported (and only imported) purchase descriptions contain the substring "imported". (this is true for the examples)
                Boolean imported = description.contains("imported");

                this.purchases.add(new purchase(amount, description, unitPrice, basicExempt, imported));
            }
        } catch (FileNotFoundException exception) {
            System.out.println(fileName + " was not found.");
        } catch (IOException exception) {
            System.out.println("I/O error while reading " + fileName);
        }
    }


    public class purchase {
        private Integer amount;
        private String description;
        private Integer unitPrice;
        private Boolean basicExempt;
        private Boolean imported;

        public purchase(Integer amount, String description, Integer unitPrice, Boolean basicExempt, Boolean imported) {
            this.amount = amount;
            this.description = description;
            this.unitPrice = unitPrice;
            this.basicExempt = basicExempt;
            this.imported = imported;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getUnitPrice() {
            return unitPrice;
        }

        public void setBasePrice(Integer unitPrice) {
            this.unitPrice = unitPrice;
        }

        public Boolean getBasicExempt() {
            return basicExempt;
        }

        public void setBasicExempt(Boolean basicExempt) {
            this.basicExempt = basicExempt;
        }

        public Boolean getImported() {
            return imported;
        }

        public void setImported(Boolean imported) {
            this.imported = imported;
        }

        public Integer getTaxDue() {
            Integer tax = 0;
            // the below calculation assumes that the two taxes are to be calculated independently.
            // The description of the problem does not make it clear if all the sales taxes are together rounded up to the nearest 0.05, or if each of them are to be rounded individually.
            // The code above assumes that the rounding is separate, but in practice I'd ask or possibly research the local law to see which was needed.
            // The difference between the two approaches is 0.05 for any cost ending in (.01 - .33) or (.51 - .66)

            // An additional assumption made here is that when two identical items are purchased at the same time, the rounding of taxes is done on the total rather than in each individual item.

            // Neither of these assumptions make a difference in the calculation of the three sample inputs (thus they must be assumed rather than being clarified by example.)

            if(!basicExempt) {
                tax += 5 * (int) Math.ceil(amount * unitPrice * 0.1 / 5);
            }
            if(imported) {
                tax += 5 * (int) Math.ceil(amount * unitPrice * 0.05 / 5);
            }
            return tax;
        }
    }
}
