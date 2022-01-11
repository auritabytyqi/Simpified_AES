public class Simplified_AES {


    public  boolean[] enkriptimi(boolean[] mesazhi, boolean[]celesi)
    {   // hyrje mesazhi 16 bit, celesi 16bit . Per me e ba run ne qet klase asht metoda statike.
        // ne kushte normale kur largohet main largohet static dhe metoda thirret nga klasat tjera 
        char[] enk=new char[4];
        boolean[] rc1={false,false,false,true};
        boolean[] rc2={false,false,true,false};
        boolean[] rc3={false,true,false,false};
        enk=addRoundKey(mesazhi,celesi);
        enk=sbox_shiftrows(enk);
        enk=mixcolumns(enk);
        boolean [] k1=gjenerimi_i_celesit(celesi,rc1);
        enk=addRoundKey(mesazhi_bin(enk),k1);
        enk=sbox_shiftrows(enk);
        enk=mixcolumns(enk);
        boolean [] k2=gjenerimi_i_celesit(k1,rc2);
        enk=addRoundKey(mesazhi_bin(enk),k2);
        enk=sbox_shiftrows(enk);
        boolean [] k3=gjenerimi_i_celesit(k2,rc3);
        enk=addRoundKey(mesazhi_bin(enk),k3);
       boolean [] enk1=mesazhi_bin(enk);
        return enk1;
    }

    public  boolean[] dekriptimi(boolean[] mesazhi, boolean[] celesi)
    {   boolean[] rc1={false,false,false,true};
        boolean[] rc2={false,false,true,false};
        boolean[] rc3={false,true,false,false};
        boolean [] k1=gjenerimi_i_celesit(celesi,rc1);
        boolean [] k2=gjenerimi_i_celesit(k1,rc2);
        boolean [] k3=gjenerimi_i_celesit(k2,rc3);
        char [] dekr=new char[4];
        dekr=addRoundKey(mesazhi, k3);
        dekr=invShiftRows(dekr);
        dekr=a_invsbox(dekr);
        dekr=addRoundKey(mesazhi_bin(dekr), k2);
        dekr=invmixcolumns(dekr);
        dekr=invShiftRows(dekr);
        dekr=a_invsbox(dekr);
        dekr=addRoundKey(mesazhi_bin(dekr), k1);
        dekr=invmixcolumns(dekr);
        dekr=invShiftRows(dekr);
        dekr=a_invsbox(dekr);
        dekr=addRoundKey(mesazhi_bin(dekr), celesi);

        boolean [] dekriptimi=mesazhi_bin(dekr);

        return dekriptimi;

    }
   
    public static boolean[] mesazhi_bin(char[] k)
    {   boolean [] m=new boolean[16];
        Binary_Hexadecimal bx=new Binary_Hexadecimal();
        boolean[] n0=bx.hex_to_bin(k[0]);
        boolean[] n1=bx.hex_to_bin(k[1]);
        boolean[] n2=bx.hex_to_bin(k[2]);
        boolean[] n3=bx.hex_to_bin(k[3]);
        for(int i=0; i<4; i++)
        {
            m[i]=n0[i];
            m[i+4]=n1[i];
            m[i+8]=n2[i];
            m[i+12]=n3[i];

        }

        return m;
    }

    public static char [] sbox_shiftrows(char[] a)
    {   Binary_Hexadecimal b=new Binary_Hexadecimal();
    
        boolean [] M0=new boolean[4];
        boolean [] M1=new boolean[4];
        boolean [] M2=new boolean[4];
        boolean [] M3=new boolean[4];
        M0=b.hex_to_bin(a[0]);
        M1=b.hex_to_bin(a[1]);
        M2=b.hex_to_bin(a[2]);
        M3=b.hex_to_bin(a[3]);
        boolean [] C0=sbox(M0);
        boolean [] C1=sbox(M1);
        boolean [] C2=sbox(M2);
        boolean [] C3=sbox(M3);
        
        // ShiftRows 
        boolean [] Ck=new boolean[4]; // varg ndihmes per zevendesim
        Ck=C1;
        C1=C3;
        C3=Ck;
        char [] shiftrows=new char[4];
        Binary_Hexadecimal bh=new Binary_Hexadecimal();
        shiftrows[0]=bh.bin_to_hex(C0);
        shiftrows[1]=bh.bin_to_hex(C1);
        shiftrows[2]=bh.bin_to_hex(C2);
        shiftrows[3]=bh.bin_to_hex(C3);
        
        return shiftrows;
    } 
    
    public static boolean [] sbox(boolean [] a)
     {  boolean [] after_sbox=new boolean[4];
         char [][] s_box=new char [4][4];
        s_box [0][0]='6';
        s_box [0][1]='B';
        s_box [0][2]='0';
        s_box [0][3]='4';
        s_box [1][0]='7';
        s_box [1][1]='E';
        s_box [1][2]='2';
        s_box [1][3]='F';
        s_box [2][0]='9';
        s_box [2][1]='8';
        s_box [2][2]='A';
        s_box [2][3]='C';
        s_box [3][0]='3';
        s_box [3][1]='1';
        s_box [3][2]='5';
        s_box [3][3]='D';
        int [] aa=new int[4];
        for (int i=0; i<aa.length; i++)
        {if(a[i])
         {aa[i]=1;}
        else {aa[i]=0;}}
        int j=aa[0]*2+aa[1]*1;
        int k=aa[2]*2+aa[3]*1;
        char c_sbox=s_box[j][k]; // vlera nga sbox
        Binary_Hexadecimal b=new Binary_Hexadecimal();
        after_sbox=b.hex_to_bin(c_sbox);
        return after_sbox;
     }
    
     public static char[] mixcolumns(char [] a)
    {   
        char [] after_mixColumns=new char[4];
        Binary_Hexadecimal bx = new Binary_Hexadecimal();
        // shendrrimi i elementeve te matrices ne stringje binare 
        boolean[] a0 = bx.hex_to_bin(a[0]);
        boolean[] a1 = bx.hex_to_bin(a[1]); 
        boolean[] a2 = bx.hex_to_bin(a[2]); 
        boolean[] a3 = bx.hex_to_bin(a[3]);
        
        //MDS matrica
        boolean[] mds1={false,false,false,true};
        // boolean[] mds2={false,false,true,false};

        // per arsye se vlera e mds eshte vetem 1 ne faktoret per D0 dhe D2 llogaritet
        boolean[] D0=new boolean[4];
        boolean[] D2=new boolean[4];
    
        boolean[] D1=new boolean[4];
        boolean[] D3=new boolean[4];

        for(int i=0; i<4; i++)
        {
            D0[i]=(mds1[i]^a0[i])^(mds1[i]^a1[i]);
            D2[i]=(mds1[i]^a2[i])^(mds1[i]^a3[i]);
        }
        boolean [] t1=new boolean[4]; // vleren e fituar pas shumezimit me mds2
        boolean[] k0=new boolean[5];// vlera e fituar pas shumezimit me mds2 kur fuqia >3
        boolean[] k1=new boolean[5];
        boolean [] k=new boolean[5];// Xor k0 dhe k1
        boolean [] p={true,false,false,true,true}; // polinomi ireducibil
        if(a[1]<'8')
        {
            for(int i=0; i<3;i++)
            {
                if(a1[i+1]==true)
                {
                   t1[i]=true;
                }
            }
            for(int i=0; i<4; i++)
            {
                D1[i]=(mds1[i]^a0[i])^(mds1[i]^t1[i]);
                
            }

        }
        else {   
            for (int i=1; i<5; i++)  
            { k0[i]=a0[i-1];}
            for(int i=0; i<4;i++)
            {
                if(a1[i]==true)
                {
                   k1[i]=true;
                }
            }
            for (int i=0; i<5; i++)  
            { k[i]=k0[i]^k1[i];}
            for (int i=0; i<4; i++)  
            { D1[i]=k[i+1]^p[i+1];}

        }
        boolean [] t2=new boolean[4];
        boolean[] r0=new boolean[5];// vlera e fituar pas shumezimit me mds2 kur fuqia >3
        boolean[] r1=new boolean[5];
        boolean [] r=new boolean[5];// Xor r0 dhe r1
        if(a[3]<'8')
        {
            for(int i=0; i<3;i++)
            {
                if(a3[i+1]==true)
                {
                   t2[i]=true;
                }
            }
            for(int i=0; i<4; i++)
            {
                D3[i]=(mds1[i]^a2[i])^(mds1[i]^t2[i]);
                
            }

        }
        else {   
            for (int i=1; i<5; i++)  
            { r0[i]=a2[i-1];}
            for(int i=0; i<4;i++)
            {
                if(a3[i]==true)
                {
                   r1[i]=true;
                }
            }
            for (int i=0; i<5; i++)  
            { r[i]=r0[i]^r1[i];}
            for (int i=0; i<4; i++)  
            { D3[i]=r[i+1]^p[i+1];}

        }

         after_mixColumns[0]=bx.bin_to_hex(D0);
         after_mixColumns[2]=bx.bin_to_hex(D2);
         after_mixColumns[1]=bx.bin_to_hex(D1);
         after_mixColumns[3]=bx.bin_to_hex(D3);
        return after_mixColumns;
        
    }

    public static boolean [] gjenerimi_i_celesit(boolean [] k0, boolean [] rc)
    {   boolean[] b0=new boolean[4];
        boolean[] b1=new boolean[4];
        boolean[] b2=new boolean[4];
        boolean[] b3=new boolean[4];
        for (int i=0; i<4; i++)
        {
            b0[i]=k0[i];
            b1[i]=k0[i+4];
            b2[i]=k0[i+8];
            b3[i]=k0[i+12];

        }
       
        boolean [] b4=new boolean[4]; //pas g
        b4=g(b3,rc);
        boolean [] ki=new boolean[k0.length]; // rez ki
        for(int i=0; i<4; i++)
        { ki[i]=b0[i]^b4[i];}
        for(int i=4; i<8; i++)
        { ki[i]=ki[i-4]^b1[i-4];}
        for(int i=8; i<12; i++)
        { ki[i]=ki[i-4]^b2[i-8];}
        for(int i=12; i<16; i++)
        { ki[i]=ki[i-4]^b3[i-12];}

  


      
      return ki;
    }
   
    public static boolean [] g(boolean [] a, boolean [] rc)
    {
        boolean [] b=new boolean[a.length]; // zhvendosja
        b[0]=a[1];
        b[1]=a[2];
        b[2]=a[3];
        b[3]=a[0];
       
        boolean [] s_box=new boolean[4];
        s_box=sbox(b);
        boolean[] rez=new boolean[4];
        for (int i=0; i<rez.length; i++)
        {
            rez[i]=s_box[i]^rc[i];
        }
        return rez;
    }
   
    public static char[] addRoundKey(boolean[] blloku,boolean[] celesi)
    {   char [] afterRoundKey=new char[4];
        Binary_Hexadecimal b=new Binary_Hexadecimal();
        // Ndarja e bllokut ne vlera vargje me nga 4 bit
        boolean [] A0=new boolean[4];
        for (int i=0; i<=3; i++)
            {A0[i]=blloku[i];}
        boolean [] A1=new boolean[4];
        for (int i=4; i<=7; i++)
            {A1[i-4]=blloku[i];}
        boolean [] A2=new boolean[4];
        for (int i=8; i<=11; i++)
            {A2[i-8]=blloku[i];}
        boolean [] A3=new boolean[4];
        for (int i=12; i<=15; i++)
            {A3[i-12]=blloku[i];}

            //Ndarja e celesit ne vlera vargje me nga 4 bit

            boolean [] K0=new boolean[4];
            for (int i=0; i<=3; i++)
                {K0[i]=celesi[i];}
            boolean [] K1=new boolean[4];
            for (int i=4; i<=7; i++)
                {K1[i-4]=celesi[i];}
            boolean [] K2=new boolean[4];
            for (int i=8; i<=11; i++)
                {K2[i-8]=celesi[i];}
            boolean [] K3=new boolean[4];
            for (int i=12; i<=15; i++)
                {K3[i-12]=celesi[i];}

        // XOR i bllokut me celsin 
        boolean [] B0=new boolean[4];
            for (int i=0; i<=3; i++)
                {B0[i]=celesi[i]^blloku[i];}
        boolean [] B1=new boolean[4];
            for (int i=4; i<=7; i++)
                {B1[i-4]=celesi[i]^blloku[i];}  
        boolean [] B2=new boolean[4];
            for (int i=8; i<=11; i++)
                {B2[i-8]=celesi[i]^blloku[i];} 
        boolean [] B3=new boolean[4];
            for (int i=12; i<=15; i++)
                {B3[i-12]=celesi[i]^blloku[i];} 
        
        afterRoundKey[0]=b.bin_to_hex(B0);
        afterRoundKey[1]=b.bin_to_hex(B1);
        afterRoundKey[2]=b.bin_to_hex(B2);
        afterRoundKey[3]=b.bin_to_hex(B3);
         return afterRoundKey;
    }
   
    public static char[] a_invsbox(char[]a)
    {   Binary_Hexadecimal b=new Binary_Hexadecimal();
        boolean [] M0=new boolean[4];
        boolean [] M1=new boolean[4];
        boolean [] M2=new boolean[4];
        boolean [] M3=new boolean[4];
        M0=b.hex_to_bin(a[0]);
        M1=b.hex_to_bin(a[1]);
        M2=b.hex_to_bin(a[2]);
        M3=b.hex_to_bin(a[3]);
        boolean [] C0=inv_sbox(M0);
        boolean [] C1=inv_sbox(M1);
        boolean [] C2=inv_sbox(M2);
        boolean [] C3=inv_sbox(M3);
        char[] after_sboxi=new char[4];
        after_sboxi[0]=b.bin_to_hex(C0);
        after_sboxi[1]=b.bin_to_hex(C1);
        after_sboxi[2]=b.bin_to_hex(C2);
        after_sboxi[3]=b.bin_to_hex(C3);

        return after_sboxi;

    }
  
    public static boolean [] inv_sbox(boolean [] a)
    {  boolean [] after_invsbox=new boolean[4];
        char [][] invs_box={
            {'2','D','6','C'},
            {'3','E','0','4'},
            {'9','8','A','1'},
            {'B','F','5','7'},

        };
        
       int [] aa=new int[4];
       for (int i=0; i<aa.length; i++)
       {if(a[i])
        {aa[i]=1;}
       else {aa[i]=0;}}
       int j=aa[0]*2+aa[1]*1;
       int k=aa[2]*2+aa[3]*1;
       char c_invsbox=invs_box[j][k]; // vlera nga sbox
       Binary_Hexadecimal b=new Binary_Hexadecimal();
       after_invsbox=b.hex_to_bin(c_invsbox);
       return after_invsbox;
    }
    
    public static char[] invShiftRows(char[] a)
    {
        char [] b=new char[a.length];
        b[0]=a[0];
        b[1]=a[3];
        b[2]=a[2];
        b[3]=a[1];
        return b;


    }
  
    public static char[] invmixcolumns(char[] a)
    {   boolean[] t00=shumezimiF(a[0]);
        boolean[] t01=shumezimiE(a[1]);
        boolean[] t10=shumezimiE(a[0]);
        boolean[] t11=shumezimiE(a[1]);
        boolean[] t20=shumezimiF(a[2]);
        boolean[] t21=shumezimiE(a[3]);
        boolean[] t30=shumezimiE(a[2]);
        boolean[] t31=shumezimiE(a[3]);
        boolean [] t0=new boolean[8];
        boolean [] t1=new boolean[8];
        boolean [] t2=new boolean[8];
        boolean [] t3=new boolean[8];
        for (int i=0; i<8; i++)
        {
            t0[i]=t00[i]^t01[i];
            t1[i]=t10[i]^t11[i];
            t2[i]=t20[i]^t21[i];
            t3[i]=t30[i]^t31[i];
        }
        t0=gf(t0);
        t1=gf(t1);
        t2=gf(t2);
        t3=gf(t3);
        boolean [] tt0=new boolean[4];
        boolean [] tt1=new boolean[4];
        boolean [] tt2=new boolean[4];
        boolean [] tt3=new boolean[4];

        for(int i=0; i<4; i++)
        {
            tt0[i]=t0[i+4];
            tt1[i]=t1[i+4];
            tt2[i]=t2[i+4];
            tt3[i]=t3[i+4];
        }
        char [] after_invmixcolumns=new char[4];
        Binary_Hexadecimal b=new Binary_Hexadecimal();
        after_invmixcolumns[0]=b.bin_to_hex(tt0);
        after_invmixcolumns[1]=b.bin_to_hex(tt1);
        after_invmixcolumns[2]=b.bin_to_hex(tt2);
        after_invmixcolumns[3]=b.bin_to_hex(tt3);

        return after_invmixcolumns;


        
    }

    public static boolean [] shumezimiF(char a)
    {
        Binary_Hexadecimal b=new Binary_Hexadecimal();
        boolean [] k=b.hex_to_bin(a);
        boolean [] a0=new boolean[8];
        boolean [] a1=new boolean[8];
        boolean [] a2=new boolean[8];
        boolean [] a3=new boolean[8];
        boolean [] an=new boolean[8];
        for (int i=0; i<4; i++)
        {
            a0[i+4]=k[i];
            a1[i+3]=k[i];
            a2[i+2]=k[i];
            a3[i+1]=k[i];

        }
        for (int i=0; i<8; i++)
        {
            an[i]=a0[i]^a1[i]^a2[i]^a3[i];
        }
        

        return an;

    }

    public static boolean[] shumezimiE(char a)
    {  Binary_Hexadecimal b=new Binary_Hexadecimal();
        boolean [] k=b.hex_to_bin(a);
        
        boolean [] a1=new boolean[8];
        boolean [] a2=new boolean[8];
        boolean [] a3=new boolean[8];
        boolean [] an=new boolean[8];
        for (int i=0; i<4; i++)
        {
            
            a1[i+3]=k[i];
            a2[i+2]=k[i];
            a3[i+1]=k[i];
            

        }
        for (int i=0; i<8; i++)
        {
            an[i]=a3[i]^a1[i]^a2[i];
        }
        

        return an;

    }
    
    public static boolean [] gf(boolean[] a)
    {   boolean [] k1=a;
        boolean [] ir1={false,false,false,true,false,false,true,true};
        boolean [] ir2={false,false,true,false,false,true,true,false};
        boolean [] ir3={false,true,false,false,true,true,false,false};
        if (a[1])
        {   for (int i=0; i<8; i++)
            {k1[i]=ir3[i]^k1[i];} }
            if (a[2])
            {
                for (int i=0; i<8; i++)
                 {k1[i]=ir2[i]^k1[i];} }
                 if(a[3])
                 {
                    for (int i=0; i<8; i++)
                    {k1[i]=ir1[i]^k1[i];}
                 }
            

        
        return k1;


    }
    }

