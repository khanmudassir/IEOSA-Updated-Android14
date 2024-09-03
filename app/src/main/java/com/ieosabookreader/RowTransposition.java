package com.ieosabookreader;

import java.util.HashMap;
import java.util.Map;

public class RowTransposition {
    private int[] key = new int[]
            { 2, 1, 3 };
    int columns;
    int rows;

    public String Encrypt(String InputText)
    {
        columns = key.length;
        rows = (int) Math.ceil((double) InputText.length() / (double) columns);
        Map<Integer, Integer> rowPosition = FillPosition(InputText);
        char[][] matrix_encrypt = new char[rows][columns];
        int charPosition = 0;
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if (charPosition < InputText.length())
                {
                    matrix_encrypt[i][j] = InputText.charAt(charPosition);
                }
                else
                {
                    matrix_encrypt[i][j] = ' ';
                }
                charPosition++;
            }
        }
        String result = "";

        for (int i = 0; i < columns; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                result += matrix_encrypt[j][rowPosition.get(i + 1)];
            }

            result += " ";
        }

        return result;
    }

    public String Decrypt(String EncryptedText) throws Exception
    {
        columns = key.length;
        rows = (int) Math.ceil((double) EncryptedText.length() / (double) columns);
        Map<Integer, Integer> rowPosition = FillPosition(EncryptedText);
        char[][] matrix_decrypt = new char[rows][columns];

        System.out.println(EncryptedText.length());

        // Fill Matrix
        int charPosition = 0;
        for (int i = 0; i < columns; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                try
                {
                    if (EncryptedText.length() > charPosition)
                    {
                        matrix_decrypt[j][rowPosition.get(i + 1)] = EncryptedText.charAt(charPosition);
                    } /*
                     * else { matrix_decrypt[j][rowPosition.get(i + 1)] = ' '; }
                     */
                }
                catch (Exception e)
                {
                    throw e;
                }
                charPosition++;
            }
        }

        String result = "";
        StringBuilder result_str = new StringBuilder();
        for (char[] cs : matrix_decrypt)
        {
            result_str.append(cs);
        }
        return result_str.toString().replaceAll("\\s+$", "");

    }

    private Map<Integer, Integer> FillPosition(String token)
    {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        for (int i = 0; i < columns; i++)
        {
            result.put(key[i], i);
        }
        return result;

    }
}