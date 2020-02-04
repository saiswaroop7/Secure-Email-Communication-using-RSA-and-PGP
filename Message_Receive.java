
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

public class Prob3_Receive {
    static int a;
    static String final_msg[] = new String[4];
    public static void main(String args[])
    {
        String msg,subst,pt;int i,j,k;
        String ct;
        Scanner x = new Scanner(System.in).useDelimiter("\\n");
        System.out.println("Enter the Sender Message:");
        msg = x.nextLine();
        final_msg = msg.split("//");
        int f[][] = new int[900][5];
        subst = "abcdefghijklmnopqrstuvwxyz ";
        char[] alphabet = subst.toCharArray();
        int[] alpha = new int[27];
        int key[] = new int[4];
        //Alphabet Integer Value
        for(i=0;i<alpha.length;i++)
        {
            alpha[i] = i;
        }
        //Alphabet Integer Value
        for(i=0;i<alpha.length;i++)
        {
            alpha[i] = i;
        }
        //Divide the Sender Message into appropriate domains
        a = Integer.parseInt(final_msg[0]);
        int sign = Integer.parseInt(final_msg[1]);
        int session_key = Integer.parseInt(final_msg[2]);
        ct = final_msg[3];
        //Using public key (A) and Obtaining Hash
        BigInteger hash = RSA_A(sign);
        int hash1 = hash.intValue();
        System.out.println("Current Hash Value: "+hash1);
        int hash2 = hashprep(ct, f, alphabet,alpha);
        //Using private key to obtain session key
        BigInteger sesskey = RSA_B(session_key);
        System.out.println("Session Key: "+sesskey);
        String obtain_key = sesskey.toString();
        if(obtain_key.length()<8)
        {
            obtain_key = String.format("%08d",sesskey);
        }
        for(i=0,j=0,k=2;i<4;i++,j+=2,k+=2)
        {
            key[i] = Integer.parseInt(obtain_key.substring(j,k));
        }
        System.out.println("\nOriginal Hash Value: "+hash2);
        
        if(hash1==hash2)
        {
            System.out.println("Verification Successful"); 
            pt = deprep(alpha,alphabet,key,ct);
            System.out.println("Decrypted Text:");
            System.out.println(pt);
        }
    }
    public static String deprep(int[] alpha, char[] alphabet, int key[],String ctt1)
    {
        String pt="";
        int i,j,k,count=0;
        char[] ctt = ctt1.toCharArray();
        i = (Math.round((ctt.length+5)/10)*10)+5;
        int[] ctf = new int[i];
        int[] ctf1 = new int[ctf.length];
        int pf[]= new int[4];
        char ct[] = new char[ctt1.length()];
        //Convert Plaintext into Number.
        for(i=0;i<ctt.length;i++)
        {
            for(j=0;j<alphabet.length;j++)
            {  
                if(ctt[i]==alphabet[j]||ctt[i]==(alphabet[j]& 0x5f))
                {
                    ctf[i] = alpha[j];
                }
            }
            if(!((ctt[i] >= 'a' && ctt[i] <= 'z') || ctt[i]==' ' || (ctt[i] >= 'A' && ctt[i] <= 'Z')))
            {
                    ctf[i] = 26;   
            }
        }
        //Pass 4 blocks for encryption.
        for(i=0,k=0;i<ctf.length;i++)
        {
            if(k<ctt.length)
            {
            for(j=0;j<4;j++,k++)
            {
                pf[j] = ctf[k];
            }
            pf = decrypt(pf,key);
            for(int ij=0;ij<4;ij++,count++)
            {
                ctf1[count] = pf[ij];
            }}
        }
        //Convert Numeric Cipher to Alphabet
        for(i=0;i<ct.length;i++)
       {
           for(j=0;j<=26;j++)
           {
           if(ctf1[i]==alpha[j])
           {
             ct[i]=alphabet[j];
             pt += ct[i];
           }}}
        return pt;
    }
    public static int[] decrypt(int[] b,int[] key)
    {
        int i;
        int[] pf = new int[4];
        for(i=0;i<4;i++)
        {
            pf[i] = b[i]-key[i];
            if(pf[i]<0)
            {
                pf[i]*=-1;
                pf[i]=27- pf[i];
            }
            else
            {
            pf[i] %= 27;
            }
        }
        return pf;
    }
    public static int hashprep(String pt, int[][] f, char[] alphabet,int[] alpha)
    {
        int i,j,k;
        char[] ptt = pt.toCharArray();
        char[] ct = new char[5];
        int[] ptf =  new int[ptt.length];
        int pf[][] = new int[5][5];
        int[] OUT = new int[5];
        int[] FOUT = new int[5];
               for(i=0;i<ptt.length;i++)
        {
            for(j=0;j<alphabet.length;j++)
            {  
                if(ptt[i]==alphabet[j])
                {
                    ptf[i] = alpha[j];
        }}}
       //5 Bit String
       for(i=0,k=0;i<f.length;i++)
       {
           if(k>=ptf.length)
             {
                 break;
             }
         for(j=0;j<f[0].length;j++)
         {
             if(k<ptf.length)
             {
             f[i][j]=ptf[k];
             k++;
             }
             else
             {
                 break;
             }}}
       int cpos = k%5,rpos = i-1;
       //Padding
       if(cpos!=0)
        {
          for(j=cpos;j<f[0].length;j++)
           { 
             f[rpos][j] = 0;
           }
        }
       for(k=0;k<rpos+1;)
       {
           for(i=0;i<5;i++,k++)
           {
           for(j=0;j<5;j++)
           {
             pf[i][j]=f[k][j];
           }
           }
           OUT = hash(rpos,OUT,pf);
       }
           for(i=0;i<5;i++)
          {
            FOUT[i] += OUT[i];
            FOUT[i]%=27;
          }
       for(i=0;i<5;i++)
       {
           for(j=0;j<=26;j++)
           {
           if(FOUT[i]==alpha[j])
           {
             ct[i]=alphabet[j];
           }}}
       System.out.println("Output Hash of Encrypted Text:");
       for(i=0;i<5;i++)
       {
           System.out.print(ct[i]);
       }
       String temp="";
       for(i=0;i<4;i++)
       {
       String format = String.format("%02d", FOUT[i]);
       temp +=format;
       }
       int rsa = Integer.parseInt(temp);
       return rsa;
    }
    public static int[] hash(int rpos, int[] OUT,int[][] f)
    {
       int sum=0,i,j,k;
       //Round 1 OUT Result
       for(i=0;i<5;i++)
       {
           for(j=0;j<5;j++)
           {
               sum += f[j][i];
           }
           OUT[i]+=sum;
           OUT[i] = OUT[i]%27;
           sum=0;
       }
       int t1,t2,t3,t4;
       //Round 2 
       for(i=1,k=0;i<5;k++,i++)
       {
           if(i==1)
           {
            t1= f[k][0];
            for(j=0;j<5;j++)
            {
               if(j+1==5)
               {
                 f[k][j]=t1;
               }
               else
               {
                   f[k][j]=f[k][j+1];
               }}
           }
           if(i==2)
           {
               t1=f[k][0];
               t2=f[k][1];
            for(j=0;j<5;j++)
            {
               if(j+2==5)
               {
                 f[k][j]=t1;
               }
               else if(j+2==6)
               {
                 f[k][j]=t2;                                    
               }
               else
               {
               f[k][j]=f[k][j+2];                
               }}
           }
           if(i==3)
           {
               t1=f[k][0];
               t2=f[k][1];
               t3=f[k][2];
            for(j=0;j<5;j++)
            {
               if(j+3==5)
               {
                 f[k][j]=t1;
               }
               else if(j+3==6)
               {
                 f[k][j]=t2;                                    
               }
               else if(j+3==7)
               {
                 f[k][j]=t3;                                    
               }
               else
               {
               f[k][j]=f[k][j+3];
               }}
           }
           if(i==4)
           {
               t1=f[k][0];
               t2=f[k][1];
               t3=f[k][2];
               t4=f[k][3];
            for(j=0;j<5;j++)
            {
               if(j+4==5)
               {
                 f[k][j]=t1;
               }
               else if(j+4==6)
               {
                 f[k][j]=t2;                                    
               }
               else if(j+4==7)
               {
                 f[k][j]=t3;                                    
               }
               else if(j+4==8)
               {
                 f[k][j]=t4;                                    
               }
               else
               {
               f[k][j]=f[k][j+4];
               }}
       }}
       //Round 2 OUT Result
       for(i=0;i<5;i++)
       {
           for(j=0;j<5;j++)
           {
               sum += f[j][i];
           }
           sum = sum%27;
           OUT[i] +=sum;
           OUT[i] = OUT[i]%27;
           sum=0;
       }
       //Round 3
       for(i=1,k=0;i<=5;k++,i++)
       {
           if(i==4)
           {
            t1= f[0][k];
            for(j=0;j<5;j++)
            {
               if(j+1==5)
               {
                 f[j][k]=t1;
               }
               else
               {
                   f[j][k]=f[j+1][k];
               }}
           }
           if(i==3)
           {             
               t1=f[0][k];
               t2=f[1][k];
            for(j=0;j<5;j++)
            {
               if(j+2==5)
               {
                 f[j][k]=t1;
               }
               else if(j+2==6)
               {
                 f[j][k]=t2;                                    
               }
               else
               {
               f[j][k]=f[j+2][k];                
               }}
           }
           if(i==2)
           {
               t1=f[0][k];
               t2=f[1][k];
               t3=f[2][k];
            for(j=0;j<5;j++)
            {
               if(j+3==5)
               {
                 f[j][k]=t1;
               }
               else if(j+3==6)
               {
                 f[j][k]=t2;                                    
               }
               else if(j+3==7)
               {
                 f[j][k]=t3;                                    
               }
               else
               {
               f[j][k]=f[j+3][k];
               }}
           }
           if(i==1)
           {
               t1=f[0][k];
               t2=f[1][k];
               t3=f[2][k];
               t4=f[3][k];
            for(j=0;j<5;j++)
            {
               if(j+4==5)
               {
                 f[j][k]=t1;
               }
               else if(j+4==6)
               {
                 f[j][k]=t2;                                    
               }
               else if(j+4==7)
               {
                 f[j][k]=t3;                                    
               }
               else if(j+4==8)
               {
                 f[j][k]=t4;                                    
               }
               else
               {
               f[j][k]=f[j+4][k];
               }}
       }}
       //Round 3 OUT Result
       for(i=0;i<5;i++)
       {
           for(j=0;j<5;j++)
           {
               sum += f[i][j];
           }
           sum = sum%27;
           OUT[i] +=sum;
           OUT[i] = OUT[i]%27;
           sum=0;
       }

       return OUT;
     }
    public static BigInteger RSA_A(int sign)
    {
            BigInteger n,phi,hash,ee;
            int p=78511,q=5657;
            //Calculating O(n)
            phi = BigInteger.valueOf((p-1)*(q-1));
            n = BigInteger.valueOf(p*q);
            BigInteger ecc;
            ee = BigInteger.valueOf((int)a);
            System.out.println("\na:"+a+" phi:"+phi+" n:"+n);
            //Verify Signature
            hash = BigInteger.valueOf(sign);
            ecc = square(hash,ee,n);
            return ecc;
    }        
    
    public static BigInteger RSA_B(int pt)
    {
             BigInteger d,n,phi,ptf,ecc;
             double k=0,e;
             int p=78511,q=5657;
             //Calculating O(n)
             phi = BigInteger.valueOf((p-1)*(q-1));
             //Public Key Value.
             n = BigInteger.valueOf(p*q);
             //Calcualting a value
             for(e=101;e<phi.intValue();e++)
             {
                 if(gcd(e,phi)==1)
                 {
                     break;
                 }
             }
            //Private Key (Extended Euclidean)
            while(true)
             {
              if((1 + (k*phi.intValue()))%e==0)
              {
               d = BigInteger.valueOf((int) ((1 + (k*phi.doubleValue()))/e));
               break;
              }
              k++;
            }
            //Encrypted Data.
            System.out.println("\nd:"+d+" e:"+e+" phi:"+phi+" n:"+n);
            //Signing 5 bit Hash Digest
            ptf = BigInteger.valueOf(pt);
            ecc = square(ptf,d,n);
            return ecc;
    }
    public static double gcd(double e,BigInteger phi)
    {
        BigInteger temp;
        while(phi.intValue()!=0)
        {
            temp = BigDecimal.valueOf(e).toBigInteger();
            e = phi.intValue();
            phi = temp.mod(phi);
        }
        return e;
    }
    public static BigInteger square(BigInteger ptf,BigInteger pow,BigInteger n)
    {
        int p=1,temp=0,power=0,j=0;
        BigInteger product,tmp,decrypt;
        product = BigDecimal.valueOf(p).toBigInteger();
        //Binary 
        int[] a = new int[100];
        String qe = Integer.toBinaryString(pow.intValue());
        char[] q = qe.toCharArray();
        for(int i=q.length-1;i>=0;i--)
        {
             temp = Character.getNumericValue(q[i]);
             if(temp==1)
             {
             a[j] = (int)Math.pow(2, power);
             j++;power++;
             }
             else
             {
               power++; 
             }
        }
        //Square and Multiply
        for(int ij =0; ij<a.length;ij++)
        {
        if(a[ij]!=0)    
        {
           tmp = BigDecimal.valueOf(p).toBigInteger();
           for(int i=0;i<=a[ij];i+=2)
                {
                    if(i==a[ij])
                    {
                        tmp = tmp.mod(n);
                        product = product.multiply(tmp);
                        break;
                    }
                    if(a[ij]!=1)
                    {
                       decrypt = ptf;
                       decrypt = decrypt.pow(2);
                       decrypt = decrypt.mod(n);
                       tmp = tmp.multiply(decrypt);
                       tmp = tmp.mod(n);
                    }
                    else
                    {
                        decrypt = ptf.pow(1);
                        decrypt = decrypt.mod(n);
                        product = product.multiply(decrypt);
                    }
                }}}
       product = product.mod(n);
       return product;
    }
}