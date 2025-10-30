INSERT INTO APP_USER (id, username) VALUES (1,'alice'), (2,'bob');

INSERT INTO CONTEST (id, name) VALUES (100,'Starter Contest');

INSERT INTO PROBLEM (id, title, contest_id, statement, sample_input, sample_output, official_input, official_output)
VALUES (
  200,
  'Sum Two Integers',
  100,
  'Read two integers and print their sum.',
  '2 3',
  '5',
  '17 25',
  '42'
);
