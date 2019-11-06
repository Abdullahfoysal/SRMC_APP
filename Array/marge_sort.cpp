#include<bits/stdc++.h>

using namespace std;
int ara[]={1,5,3,7};

/*void merge_ara(int l,int m,int r)
{
    int n1=m-l+1;
    int n2=r-m;///r-(m+1)+1
    int L[n1],R[n2];

    for(int i=0;i<n1;i++)L[i]=ara[l+i];
    for(int i=0;i<n2;i++)R[i]=ara[m+1+i];

    int k=l,i=0,j=0;

    while(i<n1 && j<n2)
    {
        if(L[i]<R[j])
        {
            ara[k]=L[i];
            i++;
        }
        else
        {
            ara[k]=R[j];
            j++;
        }

        k++;
    }
    while(i<n1)
    {
        ara[k]=L[i];
        i++;
        k++;
    }
    while(j<n2)
    {
        ara[k]=R[j];
        j++;
        k++;
    }
}
*/
void mergesort(int l,int r)
{
    if(l<r)
    {
         int m=(l+r)/2;
    mergesort(l,m);///left call==
    mergesort(m+1,r);///right call==


    //call the function merge_ara(l,m,r);or below  from where left mergesort(l,m) was first called and the next procedure will be from that position's data(l,m,r);
     int n1=m-l+1;
    int n2=r-m;///r-(m+1)+1
    int L[n1],R[n2];

    for(int i=0;i<n1;i++)L[i]=ara[l+i];
    for(int i=0;i<n2;i++)R[i]=ara[m+1+i];

    int k=l,i=0,j=0;

    while(i<n1 && j<n2)
    {
        if(L[i]<R[j])
        {
            ara[k]=L[i];
            i++;
        }
        else
        {
            ara[k]=R[j];
            j++;
        }

        k++;
    }
    while(i<n1)
    {
        ara[k]=L[i];
        i++;
        k++;
    }
    while(j<n2)
    {
        ara[k]=R[j];
        j++;
        k++;
    }
    }


}

int main()
{
mergesort(0,sizeof(ara)/sizeof(ara[0])-1);

for(int i=0;i<sizeof(ara)/sizeof(ara[0]);i++)cout<<ara[i]<<endl;

return 0;
}
