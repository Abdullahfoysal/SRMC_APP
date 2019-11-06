#include<bits/stdc++.h>


using namespace std;


int main()
{
	FILE *fp_in,*fp_out;

	char input_file[] = "scan.txt";
	char output_file[]="print.txt";

	fp_in=fopen(input_file,"r");
	fp_out=fopen(output_file,"w");

  int n1;
	fscanf(fp_in,"%d",&n1);
	int sum=n1*5;
	printf("n= %d ,sum= %d \n",n1,sum);
	fprintf(fp_out, "%d",sum);




return 0;
}