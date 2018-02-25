CREATE TABLE categoria(
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL	
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO categoria (nome) values ('Diversão');
INSERT INTO categoria (nome) values ('Lazer');
INSERT INTO categoria (nome) values ('Limpeza');
INSERT INTO categoria (nome) values ('Escolar');
INSERT INTO categoria (nome) values ('Tecnologia');
INSERT INTO categoria (nome) values ('Farmácia');
