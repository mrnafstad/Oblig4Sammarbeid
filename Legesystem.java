import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;

class Legesystem {
	static Lenkeliste<Pasient> pasienter = new Lenkeliste<Pasient>();
	static SortertLenkeliste<Lege> leger = new SortertLenkeliste<Lege>();
	static Lenkeliste<Legemiddel> legemidler = new Lenkeliste<Legemiddel>();
	static Lenkeliste<Resept> resepter = new Lenkeliste<Resept>();

	public static void main(String[] arg) {

		lesFraFil("storfil.txt");
		//lesFraFil("litenFil.txt");


		meny();

	}

	public static void meny() {
		Scanner scanner = new Scanner(System.in);
		boolean quit = false;

		skrivMeny();

		while (!quit) {

			System.out.println("\n Gjør ett valg (6 for hovedmeny)");
			int action = scanner.nextInt();
			scanner.nextLine();

			switch(action) {
			case 0:
				System.out.println("Du valgte å avslutte");
				quit = true;
				break;
			case 1: 
				skrivOversikt();
				break;
			case 2:
				System.out.println("Vil du legge til: \n");
				System.out.println(" 1: Lege \n 2: Pasient \n 3: Legemiddel \n 4: Resept \n");
				int valg = scanner.nextInt();
				scanner.nextLine();
				leggTilObjekt(valg, scanner);
				break;
			case 3:
				brukResept(scanner);
				break;
			case 4: 
				visStatistikk(scanner);
				break;
			case 5:
				skrivTilFil(scanner);
			case 6:
				skrivMeny();
				break;
			}
		}
	}

	public static void skrivMeny() {

		System.out.println("0: Avslutt \n" +
			"1: Fullstendig oversikt \n" +
			"2: Ny lege/resept/pasient/legemiddel \n" +
			"3: Bruk resept \n" +
			"4: Skriv statistikk \n" +
			"5: Skriv data til fil \n");
	}

	public static void skrivOversikt() {
		System.out.println("Resepter: \n");
		for (Resept res : resepter) {
			System.out.println(res.toString());
		}
		System.out.println("Legemidler: \n");
		for (Legemiddel mdl : legemidler) {
			System.out.println(mdl.toString());
		}
		System.out.println("Leger: \n");
		for (Lege lege : leger) {
			System.out.println(lege.toString());
		}
		System.out.println("Pasienter: \n");
		for (Pasient pas : pasienter) {
			System.out.println(pas.toString());
		}
	}

	public static void leggTilObjekt(int objekttype, Scanner scanner) {
		if (objekttype == 1) {
			System.out.println("Legens navn: \n");
			String navn = scanner.nextLine();
			System.out.println("Kontrol ID (heltall > 0 for spesialist, 0 for vanlig lege): \n");
			int kontrollID = scanner.nextInt();
			scanner.nextLine();
			if (kontrollID != 0) {
				leger.leggTil(new Spesialist(navn, kontrollID));
			} else {
				leger.leggTil(new Lege(navn));
			}
		} else if (objekttype == 2) {
			System.out.println("Navn: \n");
			String navn = scanner.nextLine();
			System.out.println("Fødselsnummer: \n");
			String fødselsnummer = scanner.nextLine();
			pasienter.leggTil(new Pasient(navn, fødselsnummer));
		} else if (objekttype == 3) {
			System.out.println("Legemidddeltype: \n Vanlig (1) \n Narkotisk (2) \n Vanedannende (3) \n");
			int type = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Navn: \n");
			String navn = scanner.nextLine();
			System.out.println("Pris: \n");
			double pris = scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Virkestoff: \n");
			double virkestoff = scanner.nextDouble();
			scanner.nextLine();

			if (type == 1) {
				legemidler.leggTil(new Vanlig(navn, pris, virkestoff));
			} else if (type == 2) {
				System.out.println("Styrke: \n");
				int styrke = scanner.nextInt();
				scanner.nextLine();
				legemidler.leggTil(new Narkotisk(navn, pris, virkestoff, styrke));
			} else if (type == 3) {
				System.out.println("Styrke: \n");
				int styrke = scanner.nextInt();
				scanner.nextLine();
				legemidler.leggTil(new Vanedannende(navn, pris, virkestoff, styrke));
			}
		} else if (objekttype == 4) {
			System.out.println("Resepttype: \n Hvit (1) \n Blå (2) \n Millitær (3) \n p (4) \n");
			int type = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Legemiddel ID: \n");
			int legemiddelID = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Pasient ID: \n");
			int pasientID = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Utskrivende lege: \n");
			String legeNavn = scanner.nextLine();

			for (Lege lege : leger) {
				if (legeNavn.compareTo(lege.hentNavn()) == 0) {
					if (type == 1) {
						System.out.println("Reit: \n");
						int reit = scanner.nextInt();
						scanner.nextLine();
						try {
							lege.skrivHvitResept(legemidler.hent(legemiddelID), pasienter.hent(pasientID), reit);
						} catch(UlovligUtskrift e) {
							System.out.println("Vanlig lege kan ikke skrive ut narkotiske legemidler");
						}
					} else if (type == 2) {
						System.out.println("Reit: \n");
						int reit = scanner.nextInt();
						scanner.nextLine();
						try {
							lege.skrivBlaaResept(legemidler.hent(legemiddelID), pasienter.hent(pasientID), reit);
						} catch(UlovligUtskrift e) {
							System.out.println("Vanlig lege kan ikke skrive ut narkotiske legemidler");
						}
					} else if (type == 3) {
						System.out.println("Reit: \n");
						int reit = scanner.nextInt();
						scanner.nextLine();
						try {
							lege.skrivMillitaerResept(legemidler.hent(legemiddelID), pasienter.hent(pasientID), reit);
						} catch(UlovligUtskrift e) {
							System.out.println("Vanlig lege kan ikke skrive ut narkotiske legemidler");
						}
					} else if (type == 4) {
						try {
							lege.skrivPResept(legemidler.hent(legemiddelID), pasienter.hent(pasientID));
						} catch(UlovligUtskrift e) {
							System.out.println("Vanlig lege kan ikke skrive ut narkotiske legemidler");
						}
					}
				}
				for (Resept res : lege.hentResepter()) {
					resepter.leggTil(res);
				}
			}
		}
	}

	public static void brukResept(Scanner scanner) {
		System.out.println("Hvem vil du se resepter for? (Velg pasient ID) \n");
		for (int i = 0; i < pasienter.stoerrelse(); i++) {
			//System.out.println("   " + pasienter.hent(i).hentNavn() +", " + pasienter.hent(i).hentFødselsnummer() + " \n" );
			System.out.println(pasienter.hent(i).toString());
		}
		int id = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Velg resept: (bruk id) \n");
		for (Resept res : pasienter.hent(id).hentResepter()) {
			System.out.println(res.toString());
		}

		int reseptID = scanner.nextInt();
		scanner.nextLine();
		resepter.hent(reseptID).bruk();
		System.out.println("Gjenverende bruk: " + resepter.hent(id).hentReit());
	}

	public static void visStatistikk(Scanner scanner) {
		System.out.println("Hvilken statistikk vil du se? \n"
							+ "(1) Totalt antall utskrevne resepter på vanedannende legemidler \n"
							+ "(2) Totalt antall utskrevne resepter på narkotiske legemidler \n"
							+ "(3) Mulig misbruk av narkotika \n");
		int valg = scanner.nextInt();
		scanner.nextLine();
		int count;

		switch (valg) {
			case 1:
				count = 0;
				for (Resept res : resepter) {
					if (res.hentLegemiddel() instanceof Vanedannende) {
						count ++;
					}
				}
				System.out.println("Resepter på vanedannende legemidler: " + count + "\n");
				break;
			case 2:
				count = 0;
				for (Resept res : resepter) {
					if (res.hentLegemiddel() instanceof Narkotisk) {
						count ++;
					}
				}
				System.out.println("Resepter på narkotiske legemidler: " + count + "\n");
				break;
			case 3:

				for (Lege lege : leger) {
					int count1 = 0;
					for (Resept res2 : lege.hentResepter()) {
						if (res2.hentLegemiddel() instanceof Narkotisk) count1 ++;
					}
					if (count1 > 0) {
						System.out.println(lege.hentNavn() + " har skrevet ut " 
											+ count1 +" resept(er) på narkotiske legemidler");
					}
				}
				for (Pasient pas : pasienter) {
					int count1 = 0;
					for (Resept res2 : pas.hentResepter()) {
						if (res2.hentLegemiddel() instanceof Narkotisk && res2.hentReit() > 0) count1 ++;
					}
					if (count1 > 0) {
						System.out.println(pas.hentNavn() + " har blitt utskrevet "
											+ count1 + " resept(er) på narkotiske legemidler");
					}
				}
			}
	}

	public static void skrivTilFil(Scanner scanner) {
		System.out.println("Filnavn: \n");
		String filnavn = scanner.nextLine();

		try {
			File nyFil = new File(filnavn);
			FileWriter skriver = new FileWriter(filnavn);
			skriver.write("# Pasienter (navn, fnr) \n");
			for (Pasient pas : pasienter) {
				String str = pas.hentNavn() + "," + pas.hentFødselsnummer() + "\n";
				skriver.write(str);
			}

			skriver.write("# Legemidler (navn,type,pris,virkestoff,[styrke]) \n");
			for (Legemiddel legemiddel : legemidler) {
				String type = "";
				String styrke = "";
				if (legemiddel instanceof Vanlig) {
					type = "vanlig,";
				} else if(legemiddel instanceof Narkotisk) {
					type = "narkotisk,";
					Narkotisk nark = (Narkotisk) legemiddel;
					styrke = Integer.toString(nark.hentNarkotiskStyrke());
				} else if (legemiddel instanceof Vanedannende) {
					type = "vanedannende,";
					Vanedannende vane = (Vanedannende) legemiddel;
					styrke = Integer.toString(vane.hentVanedannendeStyrke());
				}
				String str = legemiddel.hentNavn() + "," + type + Double.toString(legemiddel.hentPris()) 
							+ "," + Double.toString(legemiddel.hentVirkestoff()) + "," + styrke + "\n";
				skriver.write(str);
			}

			skriver.write("# Leger (navn,kontrollid / 0 hvis vanlig lege) \n");
			for (Lege lege : leger) {
				String kontrollID = "0";
				if (lege instanceof Spesialist) {
					Spesialist spes = (Spesialist) lege;
					kontrollID = Integer.toString(spes.hentKontrollID());
				}
				String str = lege.hentNavn() + "," + kontrollID + "\n";
				skriver.write(str);
			}

			skriver.write("# Resepter (legemiddelNummer,legeNavn,pasientID,type,[reit]) \n");
			for (Resept res : resepter) {
				String type = "";
				String reit = "";
				if (res instanceof HvitResept) {
					type = ",hvit,";
					reit = Integer.toString(res.hentReit());
					if (res instanceof PResept) {
						type = ",p";
						reit = " ";
					} else if (res instanceof MResept) {
						type = ",millitaer,";
						reit = Integer.toString(res.hentReit());
				}
				} else if (res instanceof BlåResept) {
					type = ",blaa,";
					reit = Integer.toString(res.hentReit());
				}
				String str = Integer.toString(res.hentLegemiddel().hentID()) + "," 
							+ res.hentLege().hentNavn() + "," + res.hentPasient().hentID()
							+ type + reit + "\n";
				skriver.write(str);
			}

			skriver.close();
		} catch(IOException e) {
			System.out.println("Det skjedde en feil");
			e.printStackTrace();
		}
	}


	// For filinnlesing

	public static void lesFraFil(String filNavn) {
		File fil = new File(filNavn);
		ArrayList<String> denneFil = new ArrayList<String>();

		try {
			Scanner myReader = new Scanner(fil);
			myReader.useDelimiter("# ");
			while (myReader.hasNext()) {
				denneFil.add(myReader.next());
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Fil ikke funnet");
		}

		//Teller for å holde styr på hvilke objekter som lages
		//0 = Pasienter, 1 = Legemidler, 2 = Leger og 3 = Resepter
		int teller = 0;

		for(String str : denneFil) {

			String[] mellom2 = str.split("\n", 0);
			if(teller == 0) {
				lagPasienter(mellom2);
				teller ++;
			} else if (teller == 1) {
				lagLegemidler(mellom2);
				teller ++;
			} else if (teller == 2) {
				lagLeger(mellom2);
				teller ++;
			} else if (teller == 3) {
				lagResepter(mellom2);
			}
		}
	}

	public static void lagPasienter(String[] str) {		
		for (int i = 1; i < str.length; i++) {
			String[] mellom1 = str[i].split(",", 0);

			pasienter.leggTil(new Pasient(mellom1[0], mellom1[1]));			
		}
	}

	public static void lagLeger(String[] str) {
		for (int i = 1; i < str.length; i++) {
			String[] mellom1 = str[i].split(",", 0);

			int kontrollID = Integer.parseInt(mellom1[1].trim());
			if (kontrollID != 0) {
				leger.leggTil(new Spesialist(mellom1[0], kontrollID));
			} else {
				leger.leggTil(new Lege(mellom1[0]));
			}
			
		}		
	}

	public static void lagLegemidler(String[] str) {
		for (int i = 1; i < str.length; i++) {
			String[] mellom1 = str[i].split(",", 0);

			String type = mellom1[1];
			String navn = mellom1[0];
			try {
				double pris = Double.parseDouble(mellom1[2]);
				double virkestroff = Double.parseDouble(mellom1[3]);

				if (type.compareTo("vanlig") == 0) {
					legemidler.leggTil(new Vanlig(navn, pris, virkestroff));
				} else if (type.compareTo("vanedannende") == 0) {
					int styrke = Integer.parseInt(mellom1[4].trim());
					legemidler.leggTil(new Vanedannende(navn, pris, virkestroff, styrke));
				} else if (type.compareTo("narkotisk") == 0) {
					int styrke = Integer.parseInt(mellom1[4].trim());
					legemidler.leggTil(new Narkotisk(navn, pris, virkestroff, styrke));
				}
			} catch(NumberFormatException e) {
			}
			
		}
	}

	public static void lagResepter(String[] str) {
		for (int i = 1; i < str.length; i++) {
			String[] mellom1 = str[i].split(",", 0);

			int legemiddelID = Integer.parseInt(mellom1[0].trim());
			int pasientID = Integer.parseInt(mellom1[2].trim());
			boolean pasISystem = false;
			for (Pasient pas : pasienter) {
				if (pasientID == pas.hentID()) {
					pasISystem = true;
				} 
			}

			boolean middelISystem = false;
			for (Legemiddel lMiddel : legemidler) {
				if (legemiddelID == lMiddel.hentID() ) {
					middelISystem = true;
				} 
			}


			if (pasISystem && middelISystem) {
				int reit = 0;

				String legeNavn = mellom1[1];
				String reseptType = mellom1[3];

				// Holdes false om vanlig lege prøver å skrive ut resept på narkotisk
				boolean lagTil = false;
				for (Lege lege : leger) {
					if (legeNavn.compareTo(lege.hentNavn()) == 0) {
						if (reseptType.compareTo("hvit") == 0) {
							try {
								reit = Integer.parseInt(mellom1[4].trim());
								lege.skrivHvitResept(legemidler.hent(legemiddelID), pasienter.hent(pasientID), reit);
								lagTil = true;
							} catch(UlovligUtskrift e) {
								System.out.println("Vanlig lege kan ikke skrive ut narkotiske legemidler");
							}
						} else if (reseptType.compareTo("blaa") == 0) {
							try {
								reit = Integer.parseInt(mellom1[4].trim());
								lege.skrivBlaaResept(legemidler.hent(legemiddelID), pasienter.hent(pasientID), reit);
								lagTil = true;
							} catch(UlovligUtskrift e) {
								System.out.println("Vanlig lege kan ikke skrive ut narkotiske legemidler");
							}
						} else if (reseptType.compareTo("millitaer") == 0) {
							try {
								reit = Integer.parseInt(mellom1[4].trim());
								lege.skrivMillitaerResept(legemidler.hent(legemiddelID), pasienter.hent(pasientID), reit);
								lagTil = true;
							} catch(UlovligUtskrift e) {
								System.out.println("Vanlig lege kan ikke skrive ut narkotiske legemidler");
							}
						} else if (reseptType.compareTo("p") == 0) {
							try {
								lege.skrivPResept(legemidler.hent(legemiddelID), pasienter.hent(pasientID));
								lagTil = true;
							} catch(UlovligUtskrift e) {
								System.out.println("Vanlig lege kan ikke skrive ut narkotiske legemidler");
							}
						}
						if (lagTil) {
							resepter.leggTil(lege.hentResepter().hent(lege.hentResepter().stoerrelse() -1));
						}
					}
				}
			}
		}		
	}

}