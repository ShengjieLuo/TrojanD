

def crop():

  fp = open("./TrojanD.txt")
  fw = open("./TrojanDRisk.txt","w")
  lines = fp.readlines()
  for line in lines :
    if len(line) > 70:
      fw.write(line)

if __name__ == "__main__":
  crop()
