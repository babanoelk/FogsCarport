package app.entities;

public class Materials {
        private int id;
        private String name;
        private int length;
        private String description;
        private int itemNumber;
        private int width;
        private int height;
        private int price;

    public Materials(int id, String name, int length, String description, int itemNumber, int width, int height, int price) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.description = description;
        this.itemNumber = itemNumber;
        this.width = width;
        this.height = height;
        this.price = price;
    }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getItemNumber() {
            return itemNumber;
        }

        public void setItemNumber(int itemNumber) {
            this.itemNumber = itemNumber;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

    }

