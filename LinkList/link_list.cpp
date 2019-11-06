#include <bits/stdc++.h>
using namespace std;

struct node
{
	int data;
	node  *next=NULL;
};
node *root,*nextnode;

node *new_node(int value)
{
	nextnode=(node*) malloc (sizeof(node));
	nextnode->data=value;
	return nextnode;

}

int main()
{
	node *linklist,*cu;
	root=new_node(5);
	cu=root;

	for(int i=0;i<5;i++)
	{
		int a;
		cin>>a;
		linklist=new_node(a);
		cu->next=linklist;
		cu=linklist;
	}

	for(int i=0;i<5;i++)
	{
		cout<<root->data<<endl;
		root=(*root).next;
	}

	/*cout<<(*root).data;
	cout<<root->data;
  */



   return 0;
}
