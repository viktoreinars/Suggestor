using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Suggestor;

namespace MovieSuggestor
{
    #region Enums
    public enum Gender
    {
        Male,
        Female
    }

    public enum Occupation
    {
        Administrator,
        Artist,
        Doctor,
        Educator,
        Engineer,
        Entertainment,
        Executive,
        Healthcare,
        Homemaker,
        Lawyer,
        Librarian,
        Marketing,
        None,
        Other,
        Programmer,
        Retired,
        Salesman,
        Scientist,
        Student,
        Technician,
        Writer
    }
    #endregion

    public class User : SuggestorUser
    {        
        private string id;
        private int age;
        private Gender gender;
        private Occupation occupation;
        private string zipcode;
        private List<Ranking> ranking;

        public int Age
        {
            get { return age; }
            set { age = value; }
        }

        public Gender Gender
        {
            get { return gender; }
            set { gender = value; }
        }

        public Occupation Occupation
        {
            get { return occupation; }
            set { occupation = value; }
        }

        public string Zipcode
        {
            get { return zipcode; }
            set { zipcode = value; }
        }

        // List of ratings
        //private Dictionary<string, Invoice> purchaseInvoices;

        public User() { }

        public User(string id, string name, int age, Gender gender, Occupation occupation, string zipcode)
        {
            this.id = id;
            this.age = age;
            this.gender = gender;
            this.occupation = occupation;
            this.zipcode = zipcode;
        }

        #region Getters/Setters

        public List<Ranking> Rankings
        {
            get { return ranking; }
            set { ranking = value; }
        }

        #endregion

        #region Suggestor Implementations

        public string Id
        {
            get
            {
                return id;
            }
            set
            {
                id = value;
            }
        }

        public List<SuggestorCollection> Collections
        {
            get
            {
                return Rankings.Cast<SuggestorCollection>().ToList();
            }
            set
            {
                Rankings = value.Cast<Ranking>().ToList();
            }
        }


        #endregion 
    }
}
