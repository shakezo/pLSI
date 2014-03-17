package main;

import java.io.BufferedReader;
import java.io.FileReader;

import util.PropertiesManager;


public class PLSI {

	int[][] n;

	//潜在変数の数
	int nz;
	//ドキュメント数
	int nd ;
	//ワード数
	int nw;


	//ライフスタイルスコア
	double[][] sl;

	//条件付き確率
	double[][][] pdwz;
	double[][] pzw;
	double[][] pzd;
	double[] pz;
	double[][] pdw;

	//入力データ：潜在クラス数,
	public PLSI(int nz,int[][] n) {


		this.n  = n;
		this.nz = nz;
		this.nd = n.length;
		this.nw = n[0].length;

		//this.n = new int[nd][nw];
		this.pdwz = new double[nd][nw][nz];
		this.pzw  = new double[nz][nw];
		this.pzd  = new double[nz][nd];
		this.pz   = new double[nz];
		this.pdw  = new double[nd][nw];

		//初期化
		this.setRand();
		System.out.println("nz="+this.nz+" nd="+this.nd+" nw="+this.nw);

	}



	//パラメタ推定
	public  void train(int k){
		double tmp =0;
		for(int i=0;i<k;i++){

			//Estepの実行
			this.e_step();

			//Mstepの実行

			this.m_step();


			//尤度差の計算
			double L = this.likelihood();
			System.out.println("k="+i+" L="+L);

			//収束判定
			if( Math.abs(L-tmp)<1.0e-10  ){
				break;
			}else{
				tmp = L;
			}
		}
	}

	//配列の初期値設定(乱数割当)
	protected void setRand(){
		for (int z =0;z<this.nz;z++){
			this.pz[z]=Math.random();
			//System.out.println("pz="+this.pz[z]);
			for(int w=0; w<this.nw;w++){
				this.pzw[z][w]=Math.random();
				for(int d=0;d<this.nd;d++){
					this.pzd[z][d]=Math.random();
					this.pdw[d][w]=Math.random();
					this.pdwz[d][w][z]=Math.random();
				}
			}
		}
	}

	//ライフスタイル制約の計算
	protected void setLifeStyleRestrict(){





	}



	//尤度計算
	protected double likelihood(){

		//p(d,w)
		for(int d=0;d<this.nd;d++){
			for(int w=0;w<this.nw;w++){
				this.pdw[d][w]=0;
				for(int z =0; z<this.nz;z++){
				  this.pdw[d][w] +=this.pz[z]*this.pzd[z][d]*this.pzw[z][w];
				}
			}
		}
		//Σn(d,w)log(d,w)
		double ll =0;
		for (int d=0;d<this.nd;d++){
			for(int w=0;w<this.nw;w++){
				 ll += this.n[d][w]*Math.log(this.pdw[d][w]);
			}
		}
		return ll;
	}

	//Estep
	protected void e_step(){
		for(int d=0; d<this.nd;d++){
			for(int w=0;w<this.nw;w++){
				double sum_pdwz =0;
				for(int z=0;z<this.nz;z++){
					sum_pdwz +=this.pz[z]*this.pzd[z][d]*this.pzw[z][w];
				}
				for(int z=0;z<this.nz;z++){
					this.pdwz[d][w][z]=this.pz[z]*this.pzd[z][d]*this.pzw[z][w]/sum_pdwz;
				}

			}
		}
		
	}


	//Mstep
	protected void m_step(){
		//p(w|z)
		for(int z=0;z<this.nz;z++){

			//規格化項の計算
			double normalization_term_pzw = 0;
			for(int w =0; w<this.nw;w++){
				for(int d =0;d<this.nd;d++){
					normalization_term_pzw += this.n[d][w]*pdwz[d][w][z];
				}
			}
			//p(w|z)の更新
			for(int w=0;w<this.nw;w++){
				this.pzw[z][w] = 0;
				for(int d =0;d<this.nd;d++){
					this.pzw[z][w] += this.n[d][w]*pdwz[d][w][z];
				}
				this.pzw[z][w] = this.pzw[z][w]/normalization_term_pzw;
			}
		}


		//System.out.println("mstep_pzw="+this.pzw[0][0]);
		//p(d|z)
		for(int z=0;z<this.nz;z++){
			double normalization_term_pzd = 0;

			//規格化項の算出
			for(int d =0; d<this.nd;d++){
				for(int w =0;w<this.nw;w++){
					normalization_term_pzd += this.n[d][w]*pdwz[d][w][z];
				}
			}
			for(int d=0;d<this.nd;d++){
				this.pzd[z][d] = 0;
				for(int w =0;w<this.nw;w++){
					this.pzd[z][d] += this.n[d][w]*pdwz[d][w][z];
				}
				this.pzd[z][d]=this.pzd[z][d]/normalization_term_pzd;
			}
		}

		//p(z)
		for(int z=0;z<this.nz;z++){
			this.pz[z] = 0;
			double normalization_term_pz = 0;
			for(int d=0;d<this.nd;d++){
				for(int w=0;w<this.nw;w++){
					normalization_term_pz +=this.n[d][w];
				}
			}
			for(int d=0;d<this.nd;d++){
				for(int w=0;w<this.nw;w++){
					this.pz[z] +=this.n[d][w]*this.pdwz[d][w][z];
				}
			}
			this.pz[z] = this.pz[z]/normalization_term_pz;
		}
	}

	public int[][] getN() {
		return n;
	}

	public int getNz() {
		return nz;
	}

	public int getNd() {
		return nd;
	}

	public int getNw() {
		return nw;
	}

	public double[][][] getPdwz() {
		return pdwz;
	}

	public double[][] getPzw() {
		return pzw;
	}

	public double[][] getPzd() {
		return pzd;
	}

	public double[] getPz() {
		return pz;
	}

	public double[][] getPdw() {
		return pdw;
	}
}
