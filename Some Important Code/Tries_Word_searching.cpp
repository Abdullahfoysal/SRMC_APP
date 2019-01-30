#include<bits/stdc++.h>
#define alphabet 26//small 

using namespace std;

struct Trie
{
	Trie *children[alphabet];
	bool isEnd;
	Trie()
	{
		for(int i=0;i<alphabet;i++)children[i]=NULL;
		
		isEnd=false;

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
			cu=cu->children[index];
		}
		else
		cu=cu->children[index];
	}

	cu->isEnd=true;
}

bool search(Trie *root,string key)
{
	Trie *cu=root;
	for(int i=0;i<key.size();i++)
	{
		int index=key[i]-'a';
		if(cu->children[index]!=NULL)
		{
			cu=cu->children[index];

		}
		else 
		{
			return false;
		}
	}

	return (cu->isEnd );
}

void deleteAll(Trie *curr)
	{
		for(int i=0;i<alphabet;i++)
		{
			if(curr->children[i]!=NULL)
			{
				
				
				deleteAll(curr->children[i]);
				
			}

				
		}
		delete curr;

	}



int main()
{
	int n=5,i=0;
	string str[n];
	while(cin>>str[i++]);

   Trie *root= new Trie();

	for(int i=0;i<n;i++)
	{
		Insert(root,str[i]);

	}
   /*Insert(root,"exit");
   Insert(root,"the");

   Insert(root,"init");
   Insert(root,"there");*/
	cout<<search(root,"exit");
	
	deleteAll(root);



	return 0;
}