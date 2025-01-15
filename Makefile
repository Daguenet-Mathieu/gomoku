NAME = main

SRCS = main.java

CLASSES = $(SRCS:.java=.class)

CC = javac
JAVA = java
FLAGS = --module-path $(PATH_TO_FX) --add-modules javafx.controls,javafx.fxml

$(CLASSES): $(SRCS)
	$(CC) $(FLAGS) $(SRCS)

all: $(CLASSES)

run: $(CLASSES)
	$(JAVA) $(FLAGS) $(NAME)

clean:
	rm -f $(CLASSES)

fclean: clean

re: fclean all

.PHONY: all clean fclean re run
