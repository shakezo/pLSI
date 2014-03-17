package main;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//入力データ生成

		//潜在変数の数
		int nz =2;


		int[][] n =  {{2,2,1,1},{1,1,2,2},{1,1,1,2}};
		PLSI plsi= new PLSI(nz,n);
		plsi.train(1000);

		System.out.println("Finish");
		System.out.println("Pz="+plsi.getPz()[0]);

	}

}
