#include <bits/stdc++.h>


using namespace std;
int position[60];
struct contest
{
	int problem;
	int penalty;
	
	bool operator<(contest n)
	{
		if(problem>n.problem)return 1;
		else if(problem==n.problem && penalty<n.penalty)return 1;
		else return 0;
	}

	
};


int main()
{
	int n,k;
	cin>>n>>k;
	std::vector<contest> v;
	contest cc;
	for (int i = 0; i < n; ++i)
	{
		int a,b;
		cin>>cc.problem>>cc.penalty;
			
		v.push_back(cc);
		
	}
	
	sort(v.begin(),v.end());
	
	pair<contest,int>p[60];
	for (int i = 0; i < 60; ++i)
	{
		p[i].second=0;
	}
	
	for (int i = 0; i < n; ++i)
	{
		cc.problem=v[i].problem;
		cc.penalty=v[i].penalty;


		if(p[i].second==0)
		{
			if(p[i].second==0)
			{

			        p[i].first.problem=cc.problem;
					p[i].first.penalty=cc.penalty;
			        p[i].second++;
			        //cout<<p[i].first.problem;
			}
			for (int j = i+1; j < n; ++j)
			{	
				if(v[i].problem==v[j].problem && v[i].penalty==v[j].penalty)
				{
					p[i].first.problem=cc.problem;
					p[i].first.penalty=cc.penalty;
			        p[i].second++;


				}
			}
			

		}
		
		
		
		
		//m.insert(pair<contest,int>(cc,i));
		//position[v[i].problem+v[i].penalty]++;

	}
	cc.problem=v[k-1].problem;
	cc.penalty=v[k-1].penalty;
	for (int i = 0; i < n; ++i)
	{
		if(p[i].first.problem==cc.problem && p[i].first.penalty==cc.penalty)
		{
			cout<<p[i].second<<endl;
			break;
		}
		
	}
	




	
		
		
		//cout<<v[i].count+1<<endl;

	
	

}