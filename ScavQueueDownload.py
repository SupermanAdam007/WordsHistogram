import json
import getpass
import urllib.parse
import urllib.request
import shutil
import os


def getTypeDetection(detectionName):
    data = [{"method": "Scans.Common.GetDetectionInfo",
        "username": username,
        "token": token,
        "params": {"scanDetection": detectionName}}]
    jsonRes = makeRequest(url, data)
    #print(jsonRes)
    return jsonRes[0]["Result"]["Fullname"]


def makeRequest(url, data):
    data = json.dumps(data)
    data = data.encode("utf-8")
    response = urllib.request.urlopen(url, data)
    res = response.read()
    encoding = response.info().get_content_charset('utf-8')
    return json.loads(res.decode(encoding))


def createFiles():
    #curr = os.getcwd()
    curr = "\\\\files-users\\USERS\\pavlat\\_private\\FPsSolver"
    try:
        os.mkdir(curr + "\\res")
    except FileExistsError:
        print("Dir: " + curr + "\\res already exists")
    try:
        os.mkdir(curr + "\\res\\Malware")
    except FileExistsError:
        print("Dir: " + curr + "\\res\\Malware already exists")
    try:
        os.mkdir(curr + "\\res\\PUP")
    except FileExistsError:
        print("Dir: " + curr + "\\res\\PUP already exists")
    try:
        os.mkdir(curr + "\\res\\Clean")
    except FileExistsError:
        print("Dir: " + curr + "\\res\\Clean already exists")
    try:
        os.mkdir(curr + "\\res\\Unknown")
    except FileExistsError:
        print("Dir: " + curr + "\\res\\Clean already exists")


class SHAFile:

    def __init__(self, jsonRes, i):
        self.storage = ""
        self.detections = []
        self.malpupclean = ""

        self.sha = jsonRes[0]["Result"][i]["FileId"]
        #print("")
        print("-File id: " + self.sha) #+ ", path: " + self.getFileInfo)
        #print("--ScanUtility is [" + jsonRes[0]["Result"][i]["ScanUtility"] + "]")
        for x in range(0, len(jsonRes[0]["Result"][i]["Detections"])):
            detectionName = jsonRes[0]["Result"][i]["Detections"][x]
            #print("---Detection: " + detectionName)
            self.detections.append(detectionName)

        print("GetPrimaryStoragePathForFile", end = ", ")
        try:
            self.storage = self.getStorage()
        except Exception:
            print("--- File not found")
        print("solveMalpupclean", end = ", ")
        print("saveToFile\n")
        self.solveMalpupclean()
        self.saveToFile()
        return

    def solveMalpupclean(self):
        if len(self.detections) == 0:
            self.malpupclean = "Clean"
            return

        self.malpupclean = "Unknown"
        for dete in self.detections:
            if (dete.find("Malware") != -1 or 
                dete.find("Trj") != -1 or 
                dete.find("Evo-gen") != -1 or 
                dete.find("FileRep") != -1 or 
                dete.find("Wrm") != -1 or 
                dete.find("Adw") != -1 or 
                dete.find("Mal") != -1):
                self.malpupclean = "Malware"
                break
            elif dete.find("[PUP]") != -1:
                self.malpupclean = "PUP"
        return

    def getStorage(self):
        data = [{"method": "Files.Common.GetPrimaryStoragePathForFile",
        "username": username,
        "token": token,
        "params": {"sha256": self.sha}}]
        jsonRes = makeRequest(url, data)
        #print(jsonRes)
        return jsonRes[0]["Result"]

    def printFileInfo(self):
        s = "-SHA256: " + self.sha + "\n"
        s += " -Path to storage: " + self.storage + "\n"
        s += " -Type: " + self.malpupclean + "\n"
        s += "  -Detections\n"
        for item in self.detections:
            s += "   -%s\n" % item
        s += "\n"
        return s
        #print("")
        #print("-SHA256: " + self.sha)
        #print(" -Path to storage: " + self.storage)
        #print("  -Detections")
        #print(*self.detections, sep="\n")

    def saveToFile(self):
        #cwd = os.getcwd()
        cwd = "\\\\files-users\\USERS\\pavlat\\_private\\FPsSolver"
        if self.malpupclean == "Malware":
            shutil.copy(self.storage, cwd + "\\res\\Malware")
        elif self.malpupclean == "PUP":
            shutil.copy(self.storage, cwd + "\\res\\PUP")
        elif self.malpupclean == "Clean":
            shutil.copy(self.storage, cwd + "\\res\\Clean")
        elif self.malpupclean == "Unknown":
            shutil.copy(self.storage, cwd + "\\res\\Unknown")
        f = open(cwd + "\\res\\info.txt", "a")
        f.write(self.printFileInfo())
        f.close()


username = "pavlat"
token = "T2NCR3pSNjRZeWhsbGJrYzhkb0tqSjROR0tCMDd3MXRwV3ZuQUNqVFN1bz01"
url = "http://scav.srv.int.avast.com:8080/api/"
#nameOfQueue = "FPsSolver-CleansetFP"
nameOfQueue = "AnalystSamples-Script"
numOfSamples = 50

# RECREATE TOKEN    
#password = getpass.getpass("Enter password for " + username + ": ")
#data = json.dumps([{"method":"Administration.Users.Login", "params":{"username": username, "password": password}}])
##print(data)
#data = data.encode('utf-8') # data should be bytes
#response = urllib.request.urlopen(url, data)
    
#data = response.read()
##print(data)
#encoding = response.info().get_content_charset('utf-8')
#jsonRes = json.loads(data.decode(encoding))
    
#token = jsonRes[0]["Result"]
#print("Token: " + token)
    
    

print("+++ TakeTopItemsFromQueue")
data = [{"method": "Files.Filters.TakeTopItemsFromQueue",
        "username": username,
        "token": token,
        "params": {"queueName": nameOfQueue,
                   "order": "Rank", #"RankDescending"
                   "maxCount": numOfSamples,
                   "minRank": 0}}]

jsonRes = makeRequest(url, data)
#print(jsonRes)



# WHICH AVs ARE AVAIBLE
#data = [{
#    "method": "Scans.Common.GetAllScanUtilities",
#    "username": username,
#    "token": token}]

#data = json.dumps(data)
#data = data.encode("utf-8")
#response = urllib.request.urlopen(url, data)

#res = response.read()
#jsonRes = json.loads(res.decode(encoding))
#print(*jsonRes[0]["Result"])


sha = jsonRes[0]["Result"]
shaList = []
for x in range(0, len(jsonRes[0]["Result"])):
    shaList.append(jsonRes[0]["Result"][x]["Sha256"])
    
#print("List of loaded shas:")
#print(*shaList, sep="\n")
#print("")

print("+++ GetFilesDetections")
scanUtil = "Avast9"
data = [{
        "method": "Scans.Common.GetFilesDetections",
        "username": username,
        "token": token,
        "params": {"shas": shaList,
                   "scanUtilities": scanUtil}
        }]

jsonRes = makeRequest(url, data)
#print(jsonRes)

createFiles()

print("+++ PARSE THE DETECTIONS")
# PARSE THE DETECTIONS
shaFiles = []
for i in range(0, len(jsonRes[0]["Result"])):
    shaFiles.append(SHAFile(jsonRes, i))

#print("len of shaList = " + str(len(shaFiles)))

#for shaFile in shaFiles:
#    shaFile.printFileInfo()

