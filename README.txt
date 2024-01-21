Pentru implementarea temei am completat clasele "MyDispatcher" si "MyHost" din schelet.

Clasa "MyDispatcher":
	-> In functia "addTask" verific politica prestabilita si gasesc host-ul caruia trebuie sa ii trimit task-ul de procesat in functie de aceasta.
	-> In cazul politicii "ROUND_ROBIN" trimit task-ul host-ului "(i + 1) % n", dupa cum este precizat si in enuntul temei, folosind o variabila
	   atomica de tip "AtomicInteger" pentru index-ul "i", deoarece astfel ma asigur ca un singur task isi va incepe executia in momentul in care
	   sunt trimise 2 sau mai multe in acelasi timp pe host-uri diferite.
	-> In cazul politicii "SHORTEST_QUEUE" folosesc functia "findMinimumQueue" pentru determinarea dintr-o lista de host-uri a aceluia ce are cea
	   mai mica coada in asteptare(adica acela pentru care valoarea host.getQueueSize() este minima).
	-> In cazul politicii "SHORTEST_INTERVAL_TASK_ASSIGNMENT" trimit task-urile in functie de tipul lor. Avand in vedere ca sunt mereu 3 host-uri
	   pentru aceasta politica, trimit primului host in cazul in care task-ul este de tip "SHORT", respectiv host-urilor 2 si 3 daca task-urile au
	   tipul "MEDIUM" si "LONG".
	-> In cazul politicii "LEAST_WORK_LEFT" folosesc functia "findLeastWorkLeftHost" pentru determinarea host-ului ce are mai putin de procesat in
	   total(adica acel host pentru care valoarea "getWorkLeft" este minima).
	-> Metodele "findLeastWorkLeftHost" si "findMinimumQueue" le-am facut sincronizate pentru a nu exista cazuri de "race condition" intre host-uri.

Clasa "MyHost":
	-> In cadrul clasei "MyHost" am utilizat o structura de date de tip "BlockingQueue" deoarece aceasta este sincronizata si blocanta(lucru ce
	   asigura executarea task-urilor cate unul pe rand) si o structura de date de tip "Set", instantiata "LinkedHashSet" si sincronizata cu
	   metoda Collections.synchronizedSet(), folosita pentru retinerea task-urilor aflate in executie(am folosit un set deoarece acesta nu
	   contine duplicate si astfel voi avea de fiecare data in executie aceleasi task-uri neincheiate).
	-> Metoda "run()" reprezinta functia pe care o ruleaza fiecare host in parte. In cadrul metodei am folosit la inceput, inainte de a incepe
	   planificarea oricarui task(adica inainte de a ajunge vreun task in coada) o secventa "Thread.sleep(50)" pentru a ma asigura ca cel putin
	   un task ajunge in coada pentru a fi posibila inceperea executiei. In interiorul metodei, pornesc un "while" care va merge atat timp cat
	   host-ul curent, care ruleaza, nu este intrerupt. Verific de fiecare data daca exista task-uri in coada, caz in care le scot si le adaug
	   in colectia ce contine elementele aflate in executie. La fiecare pas al executiei fiecare task ruleaza o singura secunda, dupa care el
	   este introdus inapoi in coada daca nu i s-a terminat timpul, sau i se da "finish()" in caz contrar si este scos din set-ul cu elementele
	   aflate in executie. La inceput, dupa ce am scos task-ul curent de procesat din coada, vreau sa verific daca in coada mai exista un alt
	   task, nepreemptibil, care nu si-a terminat executia, deoarece astfel va trebui scos acel task si pus inapoi in executie in locul task-ului
	   curent(in momentul in care un task care nu este preemptibil isi incepe executia, aceasta trebuie dusa pana la capat). Astfel, se va face 
	   switch intre task-ul curent si cel nepreemtibil din coada. In cazul in care task-ul este preemptibil, se verifica daca mai exista un alt
	   task in asteptare in coada, cu o prioritate mai mare decat a lui, caz in care task-ul curent este bagat in coada si incepe executia celui
	   cu prioritate mai mare(task-urile sunt bagate in coada in functie de prioritate).
	-> Metoda "addTask()" este folosita de "MyDispatcher" pentru adaugarea de task-uri host-urilor si in implementarea ei doar adaug task-uri
	   venite in coada.
	-> Metoda "getQueueSize()" este folosita de "MyDispatcher" pentru aflarea hostului cu coada de dimensiune minima. Sunt adunate dimensiunile
	   cozii cu task-uri care nu ruleaza si cea a set-ului cu task-uri aflate in executie.
	-> Metoda "getWorkLeft()" este folosita tot in cadrul clasei "MyDispatcher" pentru determinarea timpului de executie ramas pentru fiecare
	   host. Se determina timpul total ramas pentru fiecare task aflat atat in coada cat si in executie.
	-> Metoda "shutdown()" este folosita pentru intreruperea unui host in momentul in care nu mai sunt trimise alte task-uri pentru procesare.
	   Tot in aceasta metoda este setat campul "isInterrupted" pe "true", pentru a inceta executia "while"-ului din cadrul metodei "run()".