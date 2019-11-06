#include <bits/stdc++.h>

using namespace std;
int ara[100];
int BIT[100];
int n;

void update(int x,int delta)
{
	for(;x<=n;x+=x&(-x))
	{
		BIT[x]+=delta;
	}
}

int query(int x)
{
	int sum=0;
	for(;x>0;x-=x&(-x))
	{
		sum+=BIT[x];
	}
	return sum;
}

int main(int argc, char const *argv[])
{
	
	cin>>n;
	for(int i=1;i<=n;i++)
	{
		cin>>ara[i];
		update(i,ara[i]);
	}

	//cout<<query(2);
	printf("%d\n",query(4));

	
		
		
	return 0;
}