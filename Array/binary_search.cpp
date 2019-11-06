#include<bits/stdc++.h>

using namespace std;
vector<int>v;

int binarysearch(int l,int r,int n)
{
       int m=(l+r)/2;
        if(l>r) return 0;
        else if(v[m]> n) return binarysearch(l,m-1,n);
         if(v[m]==n) return 1;
         return binarysearch(m+1,r,n);
}
int main()
{
    int size_vector,a,search_number;
    cin>>size_vector;
   for(int i=0;i<size_vector;i++)cin>>a,v.push_back(a);

   cin>>search_number;

   int result=binarysearch(0,v.size()-1,search_number);

   if(!result)cout<<"not found";
   else cout<<"found";




    return 0;
}
