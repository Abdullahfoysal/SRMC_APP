#include <bits/stdc++.h>
using namespace std;
bool prime[1010];


void sieve(int n)
{
	for(int i=0;i<n;i++)prime[i]=true;

	for(int i=4;i<n;i+=2)prime[i]=false;//2's multiple erase

	prime[0]=prime[1]=false;

  for(int i=3;i*i<n;i+=2)///Check from 3 to +2<=n
  {
    if(prime[i]==false) continue;

    for(int j=i*i;j<n;j+=i)
    {
      prime[j]=false;
    }
  }
}

int main() {

int n=100;

	sieve(n);

  for(int i=0;i<n;i++)
  {
    if(prime[i])cout<<i<<endl;
  }
  return 0;
 }