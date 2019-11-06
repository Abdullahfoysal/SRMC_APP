#include<bits/stdc++.h>

using namespace std;
#define size1 100000
char temp[size1];

string insertfunction(string str_input,int pos,string str_insert)
{

    string temp,temp2;
    for(int i=0;i<pos;i++)temp+=str_input[i];

    for(int i=0;i<str_insert.size();i++)temp+=str_insert[i];

    for(int i=pos,j=0;i<str_input.size();i++,j++)temp2+=str_input[i];
    temp+=temp2;

    return temp;

}


int main()
{
  string  str_input,str_insert;

    cin>>str_input>>str_insert;
    int pos;
    cin>>pos;
 cout<< insertfunction(str_input,pos,str_insert);






     //for(int i=0;i<strlen(str_input)-3;i++)cout<<deletefunction(str_input,5,3)+i;


return 0;
}
