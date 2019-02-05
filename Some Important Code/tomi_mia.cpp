#include<bits/stdc++.h>

using namespace std;

#define alpha 26
int r=1;

int fact(int n)
{
	int nn=1;
	for(int i=1;i<=n;i++)
	{
		nn*=i;
	}
	return nn;

}

struct Trie
{
	Trie *children[alpha];
	int endOfWord=0;
	Trie()
	{
		endOfWord=0;
		for(int i=0;i<alpha;i++)
		{
			children[i]=NULL;
		}
	}
	
};

void Insert(Trie *root,string key)
{
	Trie *cu=root;
	for(int i=0;i<key.size();i++)
	{
		int index=key[i]-'a';
		if(cu->children[index]==NULL)
		{
			cu->children[index]= new Trie();
			
		}
		cu=cu->children[index];
	}
	cu->endOfWord++;

}

int search(Trie *root,string key)
{
	Trie *cu=root;
	for(int i=0;i<key.size();i++)
	{
		int index=key[i]-'a';
		if(cu->children[index]==NULL)
		{
			return 0;
		}
		cu=cu->children[index];

	}
	return (cu->endOfWord);
}
void Delete(Trie *root)
{
	Trie *cu=root;
	for(int i=0;i<alpha;i++)
	{
		if(cu->children[i]!=NULL)
		{
			Delete(cu->children[i]);
		}
	}
	if(cu->endOfWord)
	r*=fact(cu->endOfWord);
	delete cu;
}

void count(Trie *root)
{
	Trie *cu=root;
	for(int i=0;i<alpha;i++)
	{
		if(cu->children[i]!=NULL)
		{
			count(cu->children[i]);
		}
	}
	if(cu->endOfWord)
	r*=fact(cu->endOfWord);
	


}


int main()
{
	int t;

	cin>>t;
	getchar();
	while(t--)
	{
		
		string str="";
		char s[25];
		r=1;
		int total=0;
		
		Trie *root=new Trie();

		while(cin>>s)
		{
			Insert(root,s);
			total++;
			
			char c=getchar();
			if(c=='\n')break;
		}
     
		Delete(root);
		cout<<"1/"<<fact(total)/r<<endl;
		




		
		


	}
	
	
}