#include<bits/stdc++.h>

using namespace std;
#define size 100010
long long int ara[size];
long long int tree[size*4];
long long int tree2[size*4];

int n;

void init(int node,int b,int e)
{
	if(b==e)
	{
		tree[node]=ara[b];
		return;
	}
	int left=node*2;
	int right=node*2+1;
	int mid=(b+e)/2;
	init(left,b,mid);
	init(right,mid+1,e);
	tree[node]=tree[left]+tree[right];
}
long long int query(int node,int b,int e,int i,int j)
{
	if(b>j || e<i)return 0;
	if(b>=i && e<=j) return tree[node];
	int left=node*2;
	int right=node*2+1;
	int mid=(b+e)/2;
	long long int p1=query(left,b,mid,i,j);
	long long int p2=query(right,mid+1,e,i,j);
	return p1+p2;

}
void init2(int node,int b,int e)
{
	if(b==e)
	{
		tree2[node]=ara[b];
		return;
	}
	int left=node*2;
	int right=node*2+1;
	int mid=(b+e)/2;
	init2(left,b,mid);
	init2(right,mid+1,e);
	tree2[node]=tree2[left]+tree2[right];
}
long long int query2(int node,int b,int e,int i,int j)
{
	if(b>j || e<i)return 0;
	if(b>=i && e<=j) return tree2[node];
	int left=node*2;
	int right=node*2+1;
	int mid=(b+e)/2;
	long long int p1=query2(left,b,mid,i,j);
	long long int p2=query2(right,mid+1,e,i,j);
	return p1+p2;

}

void merge(int l,int r)
{
	int mid=(l+r)/2;
	if(l<r)
	{

		merge(l,mid);
		merge(mid+1,r);
	}
	

	int i,j,k=l;
	int n1=mid-l+1;
	int n2=r-mid;
	int L[n1],R[n2];

	for(k=l,i=0;i<n1;i++)L[i]=ara[k++];
	for(j=0;j<n2;j++)R[j]=ara[k++];

	i=0;j=0,k=l;
while(i<n1 && j<n2)
{
	if(L[i]<R[j])
	{
		ara[k++]=L[i++];

	}
	else ara[k++]=R[j++];
}
while(i<n1)
{
	ara[k++]=L[i++];
}
while(j<n2)
{
	ara[k++]=R[j++];
}

}

int main()
{
	cin>>n;
	for(int i=1;i<=n;i++)
	{
		cin>>ara[i];
	}
	init(1,1,n);
	merge(1,n);
	
	init2(1,1,n);
	int q;
	cin>>q;
	while(q--)
	{
		int a,b,c;
		cin>>a>>b>>c;
		if(a==1)cout<<query(1,1,n,b,c)<<endl;
		else cout<<query2(1,1,n,b,c)<<endl;
	}



	return 0;
}