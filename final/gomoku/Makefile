NAME = gomoku.jar
all: build move exec

clean:
	mvn clean

fclean: clean
	rm -Rf $(NAME)

build:
	mvn package -Dmaven.test.skip=true

move:
	cp target/gomoku-1.0.0-jar-with-dependencies.jar $(NAME)

re: fclean all

exec:
	java -jar gomoku.jar

.PHONY: all clean fclean build move re exec