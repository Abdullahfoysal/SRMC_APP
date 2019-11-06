#include <bits/stdc++.h>
using namespace std;

int arr[100005];
int pos[100005];
struct var
{
	int number;
	int count;
	int pos;
};
bool comp(var a,var b)
{
	if(a.count==b.count)return a.pos<b.pos;
	return a.count>b.count;
}
int main(int argc, char const *argv[])
{
	int n,a;
	cin>>n;
	for(int i=1;i<=n;i++)
	{
		cin>>a;
		arr[a]++;
		if(!pos[a])pos[a]=i;
	}
	vector<var> v;
	var temp;
	for(int i=0;i<100005;i++)
	{
		if(pos[i])
		{
			temp.number=i;
			temp.count=arr[i];
			temp.pos=pos[i];
			v.push_back(temp);
		}
	}
	sort(v.begin(), v.end(),comp);
	for (int i = 0; i < v.size(); ++i)
	{
		while(v[i].count--)cout<<v[i].number<<endl;
	}
}