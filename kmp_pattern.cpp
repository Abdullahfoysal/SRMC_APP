#include<bits/stdc++.h>

using namespace std;

int main()
{
	string text="";
	string pat="";
	int n,m;
	cin>>n>>m;
	int a;
	
	int arr[pat.size()];
	arr[0]=0;

	for(int i=1,j=0;i<pat.size();i++)
	{
		if(pat[j]==pat[i])
		{
			arr[i]=j+1;
			j++;
		}
		else
		{
			if(j-1>=0 )//&& pat[j]!=pat[i]
			{
				j=arr[j-1];

				if(pat[j]==pat[i])
				{
					arr[i]=j+1;
					j++;
				}
				else
				{
					arr[i]=0;
				}

			}
			else arr[i]=0;

			
		}
	}
	// for(int i=0;i<pat.size();i++)cout<<arr[i]<<" ";
		// cout<<endl;
    int j=0,d=0;
	for (int i = 0; i < text.size(); ++i)
	{
		if(text[i]==pat[j])
		{
			j++;
			// cout<<j<<endl;


		}
		else 
		{
			if(j-1>=0)
			{
				j=arr[j-1];
				if(text[i]==pat[j])
				{
					j++;
					
				}
			}
		}
		if(j==pat.size())
			{
				j=0;
				d++;
				
			}

	}
	cout<<d<<endl;

	return 0;
}