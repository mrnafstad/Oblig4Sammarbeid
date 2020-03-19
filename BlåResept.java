

public class BlåResept extends Resept {

	private double prisendring = 0.25;

	public BlåResept(Legemiddel legemiddel, Lege utskrivendeLege, Pasient pasient, int reit) {
		super(legemiddel, utskrivendeLege, pasient, reit);
	}

	@Override
	public String farge() {
		String blaa = "Blaa";
		return blaa;
	}

	@Override
	public double prisAaBetale() {
		return this.prisendring * this.hentLegemiddel().hentPris();
	}

	@Override
	public String toString() {
		//Bruker Legger til resepttype og bruker super.toString() for å generere resten av informasjonen
		String info = "Blå resept \n" + super.toString();
		return info;
	}
}