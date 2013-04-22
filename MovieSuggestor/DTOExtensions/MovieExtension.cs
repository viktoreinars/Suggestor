﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;
using Suggestor;

namespace MovieSuggestor
{
    [XmlType("Item")]
    public partial class Movie : SuggestorItem, SuggestorSerializeable
    {
        List<KeyValuePair<string, string>> attributes = new List<KeyValuePair<string, string>>();
        private bool genresAdded = false; // TODO: Extreme hack. Please remove

        string SuggestorItem.Id
        {
            get
            {
                return Id.ToString();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public string Description
        {
            get
            {
                return Title;
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        [XmlIgnore]
        public int Quantity
        {
            get
            {
                throw new NotImplementedException();
            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public SuggestorItem DeepCopy()
        {
            throw new NotImplementedException();
        }

        #region Extra properties for serialization



        public List<KeyValuePair<string, string>> Attributes
        {
            get
            {
                if (!genresAdded) // Ugh
                {
                    if (Unknown) attributes.Add(new KeyValuePair<string, string>("Genre", "Unknown"));
                    if (Action) attributes.Add(new KeyValuePair<string, string>("Genre", "Action"));
                    if (Adventure) attributes.Add(new KeyValuePair<string, string>("Genre", "Adventure"));
                    if (Animation) attributes.Add(new KeyValuePair<string, string>("Genre", "Animation"));
                    if (Childrens) attributes.Add(new KeyValuePair<string, string>("Genre", "Childrens"));
                    if (Comedy) attributes.Add(new KeyValuePair<string, string>("Genre", "Comedy"));
                    if (Crime) attributes.Add(new KeyValuePair<string, string>("Genre", "Crime"));
                    if (Documentary) attributes.Add(new KeyValuePair<string, string>("Genre", "Documentary"));
                    if (Drama) attributes.Add(new KeyValuePair<string, string>("Genre", "Drama"));
                    if (Fantasy) attributes.Add(new KeyValuePair<string, string>("Genre", "Fantasy"));
                    if (Film_Noir) attributes.Add(new KeyValuePair<string, string>("Genre", "Film Noir"));
                    if (Horror) attributes.Add(new KeyValuePair<string, string>("Genre", "Horror"));
                    if (Musical) attributes.Add(new KeyValuePair<string, string>("Genre", "Musical"));
                    if (Mystery) attributes.Add(new KeyValuePair<string, string>("Genre", "Mystery"));
                    if (Romance) attributes.Add(new KeyValuePair<string, string>("Genre", "Romance"));
                    if (Sci_Fi) attributes.Add(new KeyValuePair<string, string>("Genre", "Sci-Fi"));
                    if (Thriller) attributes.Add(new KeyValuePair<string, string>("Genre", "Thriller"));
                    if (War) attributes.Add(new KeyValuePair<string, string>("Genre", "War"));
                    if (Western) attributes.Add(new KeyValuePair<string, string>("Genre", "Western"));
                    genresAdded = true;
                }

                return attributes;
            }
            set
            {
                attributes = value;
            }
        }

        #endregion
    }
}
